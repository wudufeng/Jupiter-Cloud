package com.jupiterframework.channel.core;

import com.jupiterframework.channel.pojo.MessageResponse;

public interface ResponseActionListener {

    MessageResponse actionPerform(String responseData);

}
