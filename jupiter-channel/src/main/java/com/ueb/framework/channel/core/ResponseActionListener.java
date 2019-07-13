package com.ueb.framework.channel.core;

import com.ueb.framework.channel.pojo.MessageResponse;

public interface ResponseActionListener {

    MessageResponse actionPerform(String responseData);

}
