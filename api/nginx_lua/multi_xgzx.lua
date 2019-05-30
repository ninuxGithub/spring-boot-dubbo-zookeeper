--默认选项配置
local defaultDataType = 'zdsx'	--所取json文件类型
local defaultCount = 10			--默认分页每页数据条数
local compareAttr = "date1"		--合并文件时排序的字段名
local idAttr = "ID"				--去重时所用的主键字段名
local defaultMaxListLength = 100--取同一种输入参数的最大记录数（除开分页相关参数）
--配置信息，复合类型对应关系
local type_config = {
	selfSelect = "xwzx,gsgg,report"
}

--字符串分割函数
--传入字符串和分隔符，返回分割后的table
function string.split(str, delimiter)
	if str==nil or str=='' or delimiter==nil then
		return nil
	end

    local result = {}
    for match in (str..delimiter):gmatch("(.-)"..delimiter) do
        table.insert(result, match)
    end
    return result
end

--判断数组array中是否包含key值
function contains(array,key)
	if(nil == array or nil == key) then
		return false
	end

	for k, v in pairs(array) do
		if(key == v) then
			return true
		end
	end

	return false
end


--获取redis连接客户端
local function getRedisClient()
	local redis = require "resty.redis"
	--初始化redis
	local client = redis:new()
	--登录redis参数配置
	local conn_params = {
		host = "xxx",
		port = 6379,
		psw = "xxx"

	}
	--设置超时异常
	client:set_timeout(5000)
	--连接redis服务器
	local ok,err = client:connect(conn_params["host"], conn_params["port"])
	if not ok then
		error("connect redis error:"..err)
		--ngx.say("connect redis error:", err)
		return nil
	end

	--redis登录认证
	local ok,err = client:auth(conn_params["psw"])
	if not ok then
		error("redis authorized error:"..err)
		--ngx.say("redis authorized error:", err)
		return nil
	end
	return client
end

--解析参数
local function parseParams()
	local params = ngx.var.params
	if(nil == params or '' == params) then
		ngx.exit(400)
		return nil
	end

	local parsed1 = string.split(params, "&")
	local parsed2 = {}
	local parsed_params = {}
	local key,value

	if(nil == parsed1) then
		error("parameters parse error")
		return nil
	end

	for i = 1, #parsed1 do
    parsed2 = string.split(parsed1[i],"=")
		parsed_params[parsed2[1]] = parsed2[2]
	end
	return parsed_params
end

--批量获取redis中的数据
function getDataFromRedis(params)
	local client = getRedisClient()
	local codes = params["codes"]
	codes = string.split(codes,",")
	local data_type = params["type"]
	local type_arr = {}
	--处理复合类型
	if(nil ~= type_config[data_type]) then
		type_arr = string.split(type_config[data_type], ",")
	else
		type_arr = string.split(data_type,",")
	end

	--如果类型为空使用默认类型
	if(nil == data_type or '' == data_type) then
		data_type = defaultDataType
	end
	--拼接redis中的Key值
	local keys = {}
	local index = 1
	for i = 1,#codes do
		for j=1,#type_arr do
			keys[index] = codes[i].."_"..type_arr[j]
			index = index + 1
		end
	end

	--启用管道批量查询数据
	client:init_pipeline()
	for i = 1, #keys do
		client:get(keys[i])
	end
	local result,err = client:commit_pipeline()
	if not result then
		error("get data error:"..err)
		--ngx.say("get data error:", err)
		return nil
	end
	return result
end

--合并数据并返回给页面
function processData()
	local cjson = require "cjson"
	--获取并解析参数
	local params = parseParams()
	local pageNum = params["pageNum"]
	local count = params["pageSize"]
	local maxLength = params["maxLength"]
	local callbackParam = params["callbackParam"]
	local data_type = params["type"]
	--如果类型为空使用默认类型
	if(nil == data_type or '' == data_type) then
		data_type = defaultDataType
	end

	local size,maxListLength,num
	num = tonumber(pageNum)
	size = tonumber(count)
	maxListLength = tonumber(maxLength)

	if(nil == num and nil == size) then
		num = 1
		size = defaultMaxListLength
	end

	if(nil == num or num <= 0) then
		num = 1
	end

	if(nil == size or size <= 0) then
		size = defaultCount
	end
	if(nil == maxListLength or maxListLength <= 0) then
		maxListLength = defaultMaxListLength
	end

	--批量获取redis中的数据
	local result = getDataFromRedis(params)

	--如果返回结果为空，返回空值
	if(nil == result) then
		local resultMap = {}
		local resultData = {}
		resultMap["pageCount"] = 0
		resultMap["totalSize"] = 0
		resultMap[data_type] = resultData
		if(nil ~= callbackParam and '' ~= callbackParam) then
			ngx.say(callbackParam..'('..cjson.encode(resultMap)..')')
		else
			ngx.say(cjson.encode(resultMap))
		end
		return
	end
	--
	--local test = '[{"age":12,"name":"wang"},{"age":312,"name":"king"}]'
	--local table1 = cjson.decode(result[1])
	--ngx.say(table1[1]["content"])

	--用于存放各个数组下标
	local resultLen = table.getn(result)
	local indexArr = {}
	--用于存放返回数据
	local resData = {}
	--用于存放已经收集的信息ID
	local idArr = {}
	--总页数和记录总条数
	local pageCount,totalSize
	--临时变量
	local arrNo,attr,list,key,choosedNum,collectCount,resultJson,temJson,countNum
	pageCount = 0
	totalSize = 0
	choosedNum = 0
	collectCount = 0
	countNum = 0
	resultJson = {}
	temJson = {}

	--初始化每个数据集的下标为0
	for i=1,resultLen,1 do
		indexArr[i] = 1
		if(nil ~= result[i] and result[i] ~= ngx.null ) then
			resultJson[i] = cjson.decode(result[i])
			totalSize = totalSize + table.getn(resultJson[i])
		else
			resultJson[i] = nil
		end
	end


	
	--提取想要的数据
	while(true) do
		attr = nil
		for i=1,resultLen,1 do
			list = resultJson[i]
			if(nil ~= list and indexArr[i] <= table.getn(list)) then
				if(nil == attr or (nil ~= attr and attr < list[indexArr[i]][compareAttr])) then
					attr = list[indexArr[i]][compareAttr]
					arrNo = i
				end
			end
		end
		
		--console.log(attr+":"+arrNo);
		if("" == idAttr) then
			list = resultJson[arrNo]
			table.insert(resData, list[indexArr[arrNo]])
			collectCount = collectCount+1
			choosedNum = choosedNum + 1
		else
			list = resultJson[arrNo]
			key = list[indexArr[arrNo]][idAttr]
			if(not contains(idArr,key)) then
				table.insert(resData, list[indexArr[arrNo]])
				collectCount = collectCount+1
				choosedNum = choosedNum + 1
			end
		end
		
       
		indexArr[arrNo] = indexArr[arrNo]+1
		-- 所有数据排序后再跳出，区别自选股
		if(collectCount >= totalSize) then
			break
		end
	end
	
    local c = 1
	if(nil ~= resData) then
		for k,v in pairs(resData) do
		     temJson[c] = v
			 c = c+1
		end
	end
	
	--去重
	local respData = {}
	local Len = table.getn(resData)
    local flag 
	local index
	for i=1,Len,1 do
			if(i==1) then
			   table.insert(respData,resData[i])
			else
			    flag = 0
			    for j=1,i-1,1 do
					if(resData[i]["ID"]==resData[j]["ID"]) then
					  flag = 1
					  index = j
					  break     -- 查找到第一个重复的跳出
					end
				end	
                if(flag==1) then			
				    resData[index]["secuAbbr"]= resData[index]["secuAbbr"]..','..resData[i]["secuAbbr"]
					resData[index]["secuCode"]= resData[index]["secuCode"]..','..resData[i]["secuCode"]
				else
					table.insert(respData,resData[i])
				end
			end
		end
	
	
	
	
	local data2 = {}
	local len2 = table.getn(respData)
	totalSize = len2
	
	if(totalSize > maxListLength) then
		totalSize = maxListLength
		pageCount = math.ceil(totalSize/size)
	else
		pageCount = math.ceil(totalSize/size)
	end

	if(num > pageCount) then
		--ngx.exit(404)
		--ngx.say("请求的资源不存在或超出了最大记录数限制")
		local resultMap = {}
		local resultData = {}
		resultMap["pageCount"] = 0
		resultMap["totalSize"] = 0
		resultMap[data_type] = resultData
		if(nil ~= callbackParam and '' ~= callbackParam) then
			ngx.say(callbackParam..'('..cjson.encode(resultMap)..')')
		else
			ngx.say(cjson.encode(resultMap))
		end
		return
	end

	
	   for i=1,len2,1 do
				table.insert(data2, respData[i])
				countNum = countNum+1
			if(countNum >=size*num or countNum >= totalSize) then
				break
			end
		end
	
	
	local resultData = {}
	local start = (num-1)*size+1
	--ngx.say(cjson.encode(resData))
	--return
	local c = 1
	if(nil ~= data2) then
		for k,v in pairs(data2) do
			if(k >= start) then
				table.insert(resultData, v)
			end
		end
	end
	
	

	local resultMap = {}
	resultMap["pageCount"] = pageCount
	resultMap["totalSize"] = totalSize
	resultMap[data_type] = resultData
	if(nil ~= callbackParam and '' ~= callbackParam) then
		ngx.say(callbackParam..'('..cjson.encode(resultMap)..')')
	else
		ngx.say(cjson.encode(resultMap))
	end
end

--运行lua脚本文件，响应请求
processData()
