### 接口
GET  /user/login 跳转到登录页面
POST /user/login 提交登录信息
GET  /goods/list  商品列表
GET  /goods/{商品id}/detail 商品详情 
GET  /goods/{商品id}/getPath  获取秒杀接口
POST /seckill/{商品id}/{md5}/exection 执行秒杀
GET  /test/{username}/{goodsId}/kill 测试秒杀接口
POST /redis/set   @key @value set
GET  /redis/get   @key get
POST /redis/dec   @key 缓存中预先减库存
POST /seckill/{goodsId}/{path}/sendOrder @username 向消息队列发送下订单操作(包括减库存和记录订单信息)

### 端口  
seckill-eureka:8761  
seckill-view:8762  
seckill-cache:8763  
seckill-mq: 8764  
seckill-user:8001, ... , 8009  
seckill-goods:8010, ... 8019  
seckill-order:8020, ... ,8029  
zinklp:9411  
redis: 6379  
rabbit mq: 5672  


### SQL  
create table `user`(  
  `username` varchar(50),  
  `password` varchar(50),  
  primary key(`username`)  
)charset=utf8;  
  
create table `goods`(  
  `goods_id` int auto_increment COMMENT '商品ID',  
  `goods_name` varchar(64) COMMENT '商品名字',  
  `goods_img` varchar(64) comment '商品图片路径',  
  `start_time` datetime comment '秒杀开始时间',  
  `end_time` datetime comment '秒杀结束时间',  
  `goods_price`       float DEFAULT '0.00'  COMMENT '商品单价',  
  `goods_stock`       int       DEFAULT '0'     COMMENT '商品库存，-1表示没有限制',  
  PRIMARY KEY (`goods_id`)  
)charset=utf8;  

create table `order`(  
  `goods_id` int,  
  `username` varchar(50),  
  `order_time` datetime comment '订单时间',  
  primary key(`goods_id`, `username`),  
  foreign key(`goods_id`) references `goods`(`goods_id`),  
  foreign key(`username`) references `user`(`username`)  
)charset=utf8;  