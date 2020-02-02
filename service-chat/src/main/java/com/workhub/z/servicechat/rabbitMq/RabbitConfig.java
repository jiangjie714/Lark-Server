package com.workhub.z.servicechat.rabbitMq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 Broker:它提供一种传输服务,它的角色就是维护一条从生产者到消费者的路线，保证数据能按照指定的方式进行传输,
 Exchange：消息交换机,它指定消息按什么规则,路由到哪个队列。
 Queue:消息的载体,每个消息都会被投到一个或多个队列。
 Binding:绑定，它的作用就是把exchange和queue按照路由规则绑定起来.
 Routing Key:路由关键字,exchange根据这个关键字进行消息投递。
 vhost:虚拟主机,一个broker里可以有多个vhost，用作不同用户的权限分离。
 Producer:消息生产者,就是投递消息的程序.
 Consumer:消息消费者,就是接受消息的程序.
 Channel:消息通道,在客户端的每个连接里,可建立多个channel.
 */

@Configuration
public class RabbitConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;


    public static final String EXCHANGE_CONTACT = "exchange_contact";

    public static final String EXCHANGE_SYSMSG = "exchange_sysmsg";
    //public static final String EXCHANGE_C = "my-mq-exchange_C";


    public static final String QUEUE_CONTACT = "queue_contact";
    //群消息队列
    public static final String QUEUE_GROUPEDIT = "queue_groupedit";

    public static final String QUEUE_SYSMSG = "queue_sysmsg";
    /**群(会议)变更消息队列*/
    public static final String QUEUE_GROUPCHANGE = "queue_groupchange";
    /**群(会议)审批日志队列*/
    public static final String QUEUE_GROUP_APPROVELOG = "queue_group_approvelog";
    //public static final String QUEUE_C = "QUEUE_C";

    public static final String ROUTINGKEY_CONTACT = "routingkey_contact";
    //群信息编辑队列路由
    public static final String ROUTINGKEY_GROUPEDIT = "routingkey_groupedit";

    //群(会议)变更消息队列路由
    public static final String ROUTINGKEY_GROUPCHANGE = "routingkey_groupchange";
    //群(会议)审批日志消息队列路由
    public static final String ROUTINGKEY_GROUPAPPROVELOG = "routingkey_group_approvelog";

    public static final String ROUTINGKEY_SYSMSG = "routingKey_sysmsg";
    //public static final String ROUTINGKEY_C = "spring-boot-routingKey_C";

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host,port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }
    //消费者监听
    @Bean(name = "simpleRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }


    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     HeadersExchange ：通过添加属性key-value匹配
     DirectExchange:按照routingkey分发到指定队列
     TopicExchange:多关键字匹配
     */
    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(EXCHANGE_CONTACT);
    }
    /**
     * 获取队列A
     * @return
     */
    @Bean
    public Queue queueContact() {
        return new Queue(QUEUE_CONTACT, true); //队列持久
    }
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queueContact()).to(defaultExchange()).with(RabbitConfig.ROUTINGKEY_CONTACT);
    }

    @Bean
    public DirectExchange MsgExchange() {
        return new DirectExchange(EXCHANGE_SYSMSG );
    }
    /**
     * 获取队列A
     * @return
     */
    @Bean
    public Queue queueMsg() {
        return new Queue(QUEUE_SYSMSG , true); //队列持久
    }
    @Bean
    public Binding Msgbinding() {
        return BindingBuilder.bind(queueMsg()).to(MsgExchange()).with(RabbitConfig.ROUTINGKEY_SYSMSG );
    }
   //群编辑队列
    @Bean
    public Queue queueGroupEdit() {
        return new Queue(QUEUE_GROUPEDIT, true); //队列持久
    }
    //群编辑绑定队列
    @Bean
    public Binding groupEditbinding() {
        return BindingBuilder.bind(queueGroupEdit()).to(defaultExchange()).with(RabbitConfig.ROUTINGKEY_GROUPEDIT );
    }

    /**群（会议）变更队列 true 表示队列持久*/
    @Bean
    public Queue queueGroupChange() {
        return new Queue(QUEUE_GROUPCHANGE, true);
    }
    /**群（会议）变更日志绑定队列*/
    @Bean
    public Binding groupChangebinding() {
        return BindingBuilder.bind(queueGroupChange()).to(defaultExchange()).with(RabbitConfig.ROUTINGKEY_GROUPCHANGE );
    }
    /**群（会议）审批日志队列 true 表示队列持久*/
    @Bean
    public Queue queueGroupApprove() {
        return new Queue(QUEUE_GROUP_APPROVELOG, true);
    }
    /**群（会议）变更日志绑定队列*/
    @Bean
    public Binding groupApprovebinding() {
        return BindingBuilder.bind(queueGroupApprove()).to(defaultExchange()).with(RabbitConfig.ROUTINGKEY_GROUPAPPROVELOG );
    }
}