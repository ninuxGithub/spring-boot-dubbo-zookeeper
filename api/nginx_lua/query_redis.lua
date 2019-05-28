local redis = require "resty.redis"
local red = redis:new()
local host = '127.0.0.1'
local port = 6379
local psw = 'redis'
local redis_key = ngx.var.redis_key

red:set_timeout(5000) -- 1 sec

local ok, err = red:connect(host, port)
if not ok then
  error("failed to connect: "..err)
  --ngx.say("failed to connect: ", err)
  return
end

local authed, err = red:auth(psw)
if not authed then
        error("failed to authenticate: "..err)
    --ngx.say("failed to authenticate: ", err)
        return
end

if redis_key == "HS_Light_Token_KEY_STR" then
  local db1, err = red:select(2)
  if not db1 then
    error("failed to select db1: "..err)
    --ngx.say("failed to get data: ", err)
    return
  end
end

local res, err = red:get(redis_key)
if not res then
  error("failed to get data: "..err)
  --ngx.say("failed to get data: ", err)
  return
end

if res == ngx.null then
  ngx.exit(404)
  --ngx.say("data not found.")
  return
  --local cjson = require "cjson"
  --res = cjson.encode({})
end

ngx.say(res)

-- or just close the connection right away:
local ok, err = red:close()
if not ok then
  error("failed to close: "..err)
  --ngx.say("failed to close: ", err)
  return
end
