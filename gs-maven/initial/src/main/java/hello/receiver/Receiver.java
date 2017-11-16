package hello.receiver;

import hello.domain.Email;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


/**
 * 这个类用于接收JMS发送的消息，也被称为消息驱动的POJO
 */
@Component
public class Receiver {

    /**
     * JmsListener注释定义了此方法应侦听的Destination的名称以及对JmsListenerContainerFactory的引用，这个引用用于创建基础消息侦听器容器
     * 严格地说，最后一个属性是没有必要的，除非你需要定制容器的构建方式，因为Spring Boot会在必要时注册一个默认工厂。
     * @param email
     */
    @JmsListener(destination = "mailbox", containerFactory = "myFactory")
    public void receiveMessage(Email email) {
        System.out.println("Received <" + email + ">");
    }
}
