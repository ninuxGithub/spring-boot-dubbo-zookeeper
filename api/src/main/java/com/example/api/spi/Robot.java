package com.example.api.spi;

import com.alibaba.dubbo.common.extension.SPI;

/**
 * @author shenzm
 * @date 2019-2-15
 * @description 作用
 */

@SPI
public interface Robot {
    void sayHello();
}
