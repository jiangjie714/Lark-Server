# 复杂产品协同研发平台

##maven包导入出现问题的解决方法：
  慢的问题：使用IDEA编辑器，点击File->Settings,选择maven,在user setting file后面勾选override,查看路径下是否有settings.xml文件，如果没有就创建一个，并复制内容
`<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                            https://maven.apache.org/xsd/settings-1.0.0.xsd">
  
        <mirrors>
          <mirror>  
              <id>alimaven</id>  
              <name>aliyun maven</name>  
              <url>http://maven.aliyun.com/nexus/content/groups/public/</url>  
              <mirrorOf>central</mirrorOf>          
          </mirror>  
        </mirrors>
  </settings>`
  保存，然后maven reimport。
  
  引s入包还是红色的问题：把pom.xml里面的依赖注释掉，然后把maven clean一下，然后把刚才注释的依赖取消注释。。