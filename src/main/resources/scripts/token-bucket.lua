local tokenKey = KEYS[1]
local lstRefillKey = KEYS[2]

local capacity = tonumber(ARGV[1])
local now = tonumber(ARGV[2])
local refillRate = tonumber(ARGV[3])

local tokens = redis.call('GET', tokenKey)
local lastRefillTime = redis.call('GET', lstRefillKey)

if not tokens then
    tokens = capacity
else
    tokens = tonumber(tokens)
end

if not lastRefillTime then
    lastRefillTime = now
else
    lastRefillTime = tonumber(lastRefillTime)
end

local elapsedMins = math.floor((now - lastRefillTime) / 60000)

if elapsedMins > 0 then
    local refillTokens = elapsedMins * refillRate

    tokens = math.min(
            capacity,
            tokens + refillTokens
    )

    lastRefillTime = lastRefillTime + (elapsedMins * 60000)
end

if tokens <= 0 then
    return 0
end

tokens = tokens - 1

redis.call('SET', tokenKey, tokens)
redis.call('SET', lstRefillKey, lastRefillTime)

return 1