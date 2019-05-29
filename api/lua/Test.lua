---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by shenzm.
--- DateTime: 2019-5-29 10:11
---

function main()
    print("Hello World")

    a = nil;
    b = nil;
    print(a);
    print(b);
    print(type("Hello World"))
    print(type(10.4 * 3))
    print(type(print))
    print(type(type))
    print(type(true))
    print(type(nil))
    print(type(type(X)))
    print(X)

    tab = { key1 = "val1", key2 = "val2" }
    for k, v in pairs(tab) do
        print(k .. "-" .. v)
    end
    print('通过key获取value : ' .. tab['key1'])
    print('-------------------------------------------------')

    if type(X) == "nil" then
        print("true")
    else
        print("false");
    end

    if false or nil then
        print("false or nil 有一个为 true")
    else
        print("false or nil 都为 false")
    end

    print('-------------------------------------------------')

    local tab = { "apple", "pear", "orange", "grape" }
    for k, v in pairs(tab) do
        print(k .. "  table 元素有：" .. v)
    end

    --table 的角标是从1开始的
    print("通过角标来获取table: " .. tab[1])
    print("通过角标来获取table: " .. tab[2])
    print("通过角标来获取table: " .. tab[3])
    print("通过角标来获取table: " .. tab[4])

    print('-------------------------------------------------')

    print('斐波那契数列： ' .. fbnq(10))

    print('-------------------------------------------------')



    testFunc(tab, function(k, v)
        return k .. "-------函数传递参数（函数）-------" .. v;
    end);



    print('-------------------------------------------------')

    array = {"Lua", "Java"}
    for i = 0,2 do
        print(array[i])
    end

    array ={};
    for i = -2, 2 do
        array[i] = i * 2;
    end
    for i = -2, 2 do
        print(array[i])
    end
    --print(tostring(array))



end

function testFunc(tab, func)
    for k, v in pairs(tab) do
        print(func(k, v))
    end

end

function fbnq(n)
    if n == 0 then
        return 1
    else
        return n * fbnq(n - 1)
    end
end

main()
