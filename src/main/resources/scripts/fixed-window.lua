local current = redis.call('INCR', KEYS[1])

if current == 1 then
    redis.call('PEXPIRE', KEYS[1], ARGV[1])
end

if current <= tonumber(ARGV[2]) then
    return 1
else
    return 0
end