---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by shenzm.
--- DateTime: 2019-5-30 15:35
---



function main()

    local tab = { 1, 2, 3 }
    print(tabLength(tab))

    local params = parseParams();
    print(tabLength(params))
    for k, v in pairs(params) do
        print(k .. "  " .. v)
    end
end

--获取table的长度
function tabLength(tab)
    local len = 0;
    for i, k in pairs(tab) do
        len= len +1;
    end
    return len;
end


--解析参数
function parseParams()
    local params = "type=002697_xwzx_1&type2=002697_xwzx"
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
--function contains(array, key)
--    if (nil == array or nil == key) then
--        return false
--    end
--
--    for k, v in pairs(array) do
--        if (key == v) then
--            return true
--        end
--    end
--
--    return false
--end
main()