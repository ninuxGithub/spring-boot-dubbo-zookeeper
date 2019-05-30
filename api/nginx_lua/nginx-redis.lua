--携带多个参数， 合并redis的请求结果一起返回给前端
--eg 请求方式：http://10.1.51.240:9080/redis-test/json/?type=002697_xwzx_1,002697_xwzx
--默认选项配置
local defaultDataType = 'zdsx'    --所取json文件类型
--配置信息，复合类型对应关系
local type_config = {
    selfSelect = "002697_xwzx_1,002697_xwzx"
}

--字符串分割函数
--传入字符串和分隔符，返回分割后的table
function string.split(str, delimiter)
    if str == nil or str == '' or delimiter == nil then
        return nil
    end

    local result = {}
    for match in (str .. delimiter):gmatch("(.-)" .. delimiter) do
        table.insert(result, match)
    end
    return result
end

--判断数组array中是否包含key值
function contains(array, key)
    if (nil == array or nil == key) then
        return false
    end

    for k, v in pairs(array) do
        if (key == v) then
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
        host = "127.0.0.1",
        port = 6379,
        psw = "redis"
    }
    --设置超时异常
    client:set_timeout(5000)
    --连接redis服务器
    local ok, err = client:connect(conn_params["host"], conn_params["port"])
    if not ok then
        error("connect redis error:" .. err)
        return nil
    end

    --redis登录认证
    local ok, err = client:auth(conn_params["psw"])
    if not ok then
        error("redis authorized error:" .. err)
        return nil
    end
    return client
end

--解析参数
local function parseParams()
    local params = ngx.var.params
    if (nil == params or '' == params) then
        ngx.exit(400)
        return nil
    end

    local parsed1 = string.split(params, "&")
    local parsed2 = {}
    local parsed_params = {}
    local key, value

    if (nil == parsed1) then
        error("parameters parse error")
        return nil
    end

    for i = 1, #parsed1 do
        parsed2 = string.split(parsed1[i], "=")
        parsed_params[parsed2[1]] = parsed2[2]
    end
    return parsed_params
end

--批量获取redis中的数据
function getDataFromRedis(params)
    local client = getRedisClient()
    local data_type = params["type"]
    local type_arr = {}

    --处理复合类型
    if (nil ~= type_config[data_type]) then
        type_arr = string.split(type_config[data_type], ",")
    else
        type_arr = string.split(data_type, ",")
    end

    --如果类型为空使用默认类型
    if (nil == data_type or '' == data_type) then
        data_type = defaultDataType
    end
    --拼接redis中的Key值
    local keys = {}
    local index = 1
    for j = 1, #type_arr do
        keys[index] = type_arr[j]
        index = index + 1
    end


    --启用管道批量查询数据
    client:init_pipeline()
    for i = 1, #keys do
        client:get(keys[i])
    end
    local result, err = client:commit_pipeline()
    if not result then
        error("get data error:" .. err)
        return nil
    end
    return result
end

--合并数据并返回给页面
function processData()
    local cjson = require "cjson"
    local params = parseParams()
    local data_type = params["type"]
    local res = {}
    local key = string.split(data_type, ",")
    local resultMap = {}
    --批量获取redis中的数据
    local result = getDataFromRedis(params)
    for i = 1, #result do
        if (nil ~= result[i] and result[i] ~= ngx.null) then
            res[key[i]] = cjson.decode(result[i])
        end
    end
    if (nil ~= callbackParam and '' ~= callbackParam) then
        ngx.say(callbackParam .. '(' .. cjson.encode(res) .. ')')
    else
        ngx.say(cjson.encode(res))
    end
end

--运行lua脚本文件，响应请求
processData()
