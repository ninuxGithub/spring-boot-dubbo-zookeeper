--receive request params
local request_method = ngx.var.request_method
local args = nil
local key = nil
local value = nil
--获取参数的值
if "GET" == request_method then
    args = ngx.req.get_uri_args()
elseif "POST" == request_method then
    ngx.req.read_body()
    args = ngx.req.get_post_args()
end
key = args["key"]
value = args["value"]
--connect redis
local redis = require "resty.redis"
local cache = redis.new()
local ok, err = cache.connect(cache, '127.0.0.1', '6379')
cache:set_timeout(60000)
if not ok then
    ngx.say("failed to connect:", err)
    return
end
--密码
cache:auth('redis')

local res, err = cache:get(key)
if not res then
    ngx.say("failed to get " .. key .. ": ", err)
    return
end

if res == ngx.null then
    ngx.say(key .. " not found.")
    return
end

ngx.say(res)
local ok, err = cache:close()
if not ok then
    ngx.say("failed to close:", err)
    return
end
