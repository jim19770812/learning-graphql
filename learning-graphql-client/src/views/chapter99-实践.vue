<template>
  <div>
    <h1>第99章 实践</h1>
    在本章中，结合前面学到的知识，做一系列的小功能来验证
    <ul>
      <li>全部都经过了实际调试和验证</li>
      <li>服务器端采用springboot+graphql编写</li>
    </ul>
    <h2>99.1 maven依赖</h2>
    <pre>
  &lt;dependency&gt;
    &lt;groupId&gt;<strong>com.graphql-java-kickstart</strong>&lt;/groupId&gt;
    &lt;artifactId&gt;<strong>graphql-spring-boot-starter</strong>&lt;/artifactId&gt;
    &lt;version&gt;5.3.1&lt;/version&gt;
  &lt;/dependency&gt;
  &lt;dependency&gt;
    &lt;groupId&gt;<strong>com.graphql-java-kickstart</strong>&lt;/groupId&gt;
    &lt;artifactId&gt;<strong>graphiql-spring-boot-starter</strong>&lt;/artifactId&gt;
    &lt;version&gt;5.3.1&lt;/version&gt;
  &lt;/dependency&gt;
  &lt;dependency&gt;
    &lt;groupId&gt;<strong>com.graphql-java-kickstart</strong>&lt;/groupId&gt;
    &lt;artifactId&gt;<strong>graphql-java-tools</strong>&lt;/artifactId&gt;
    &lt;version&gt;5.4.1&lt;/version&gt;
  &lt;/dependency&gt;
    </pre>

    <h2>99.2 类型MUser和Job</h2>
    <ul>
      <li>这两个是基础的类型，其中MUser和Job是一对多的</li>
      <ul>在*.graphqls文件中#打头的是注释，注释会自动在前端的graphiql里以文档的形式显示，很方便</ul>
    </ul>
    后端的graphql文件定义
    <pre>
  <strong>/resources/graphql/schema.graphqls</strong>
  #用户对象
  type MUser {
    #用户ID
    id: String,
    #姓名
    name: String!,
    # 性别
    gender: String!,
    #年龄
    age: Int!,
    #入伙时间
    joinDate: String,
    #称号
    titles: [String!]!
    #first():MUser
    #根据名字获取工作，在对应的springboot-graphql后台方法中会映射成一个getter方法，注意这个getter方法的首个参数一定当前类的实例（本例中是MUser）
    <strong>job(name : String): Job</strong>
  }

  #工作
  type Job {
    #工作名称
    name : String!
    #薪水
    salary : Float
  }
    </pre>
    上面两个类对应的java代码如下
    <pre>
  <strong>MUser.java</strong>
  @Getter @Setter
  @ToString
  @EqualsAndHashCode
  @Document(value = "m_user")
  public class MUser {
      @Id
      @Accessors(chain = true)
      private String id;
      @Indexed
      @Field("user_name")
      @Accessors(chain = true)
      private String name;
      @Field("gender")
      @Accessors(chain = true)
      private String gender;
      @Field("age")
      @Accessors(chain = true)
      private Integer age;
      @Field("join_date")
      @Accessors(chain = true)
      @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
      private Date joinDate;
      @Field("titles")
      @Accessors(chain = true)
      private List&lt;String&gt; titles;
  }

  <strong>MUserResolver.java</strong>

  /**
  * MUser的Resolver，Resolver和Entity组合起来实现Schema所定义的功能，Entity定义纯粹的属性，Resolver实现属性的getter方法
  */
  @Component
  public class MUserResolver implements GraphQLResolver&lt;MUser&gt; {
      // @Resource
      // private MongoTemplate mongoTemplate;

      <strong>public Job getJob(MUser user, String name)</strong>{
          Job result=new Job();
          result.setName("干饭");
          result.setSalary(100.0);
          return result;
      }
  }

  @Getter
  @Setter
  @ToString
  @EqualsAndHashCode
  @Document(value = "job")
  public class Job {
      @Id
      private String id;
      @Field("job_name")
      @Accessors(chain = true)
      private String name;
      @Field("job_salary")
      @Accessors(chain = true)
      private Double salary;
  }
    </pre>
    <ul>
      <li>上例中可以看出，MUser中有个job(name: String): Job的方法定义，这是一个带参数的字段</li>
      <li>由MUserResolver负责解析，注意MUserResolver.getJob方法中的地一个参数是属主对象MUser的实例</li>
      <li>MUserResolver的方法前面有个get，说明这是个getter方法，只有这样才能被识别</li>
      <ul>Muser+MuserResolver联合起来实现了GraphQL总定义的类型MUser</ul>
    </ul>

    <h2>99.3 类型Query的定义</h2>
    <ul>
      <li>Query类型相当与Graphql的查询入口，所有的查询功能都要在这里定义</li>
      <li>Query只能定义一次，并且名字是固定的</li>
      <li>GraphQL类型String[]要映射成List&lt;String&gt;，不能映射成String[]，这点要注意</li>
    </ul>
    <pre>
  <strong>/resources/graphql/root.graphqls</strong>

  #查询的入口，所有查询都要从Query类型里定义
  type Query {
      #按照姓名查询用户
      user(usrName: String): MUser!
      #首个用户
      firstUser:MUser #不能加括号，加上会报错
      # 工作列表
      jobs : [Job!]!
      #工作名字列表
      # jobNames : [String!]
  }

  #定义的schema入口，里面只有query和mutation元素
  schema{
      query : Query
  }

  <strong>QueryResolver.java</strong>
  @Component
  public class QueryResolver implements GraphQLQueryResolver {
      @Resource
      private MUserResolver userResolver;

      public MUser user(String usrName){
          MUser result=new MUser();
          result.setName("jim");
          result.setAge(30);
          result.setId("1");
          result.setGender("male");
          result.setTitles(Lists.newArrayList("武将"));
          return result;
      }

      public MUser firstUser(){
          MUser result=new MUser();
          result.setName("jim");
          result.setAge(30);
          result.setId("1");
          result.setGender("male");
          result.setTitles(Lists.newArrayList("武将"));
          return result;
      }

      public List&lt;Job&gt; jobs(){
          Job job=new Job();
          job.setName("武将");
          job.setSalary(20000.0);
          return Lists.newArrayList(job);
      }
  }
    </pre>
    <ul>
      <li>注意QueryResolver中的定义死type Query中方法的映射</li>
      <li>和type映射一样，String[]要映射成List&lt;String&gt;</li>
    </ul>
  </div>

  <h2>99.4 Mutations定义</h2>
  <ul>
    <li>Muations是用于处理变更的</li>
    <li>Muations和Query一样，只能定义一次</li>
  </ul>
  <pre>
  <strong>/resources/graphqls/root.graphqls</strong>
  #添加用户
  input <strong>TAddUser</strong>{
      #用户名
      name: String!
      #性别
      gender: String!
      #年龄
      age: Int!
  }

  #更新用户对象
  input <strong>TUpdateUser</strong>{
      #ID
      id: String!
      #姓名
      name: String
      #性别
      gender: String
      #年龄
      age: Int
      #入伙时间
      joinDate: String @date
      titles: [String!]
  }

  #变更
  type Mutation{
      #添加用户
      addUser(user: TAddUser): MUser!
      updateUser(user: TUpdateUser!): MUser!
  }

  <strong>TAddUser.java</strong>
  @Getter @Setter
  @ToString
  @EqualsAndHashCode
  public class <strong>TAddUser</strong> {
      private String name;
      private String gender;
      private Integer age;    
  }

  <strong>TUpdateUser.java</strong>
  @EqualsAndHashCode
  @ToString
  @Getter @Setter
  public class <strong>TUpdateUser</strong> {
      private String id;
      private String name;
      private String gender;
      private Integer age;
      private Date joinDate;
      private List&lt;String&gt; titles;
      
  }
  <strong>Mutation.java</strong>
  @Slf4j
  @Component
  public class Mutation implements GraphQLMutationResolver {

      /**
      * 添加用户
      * @param user
      * @return
      */
      public MUser addUser(<strong>TAddUser</strong> user){
          MUser result=new MUser();
          result.setName("吕布");
          result.setAge(20);
          result.setGender("男");
          result.setId("1");
          result.setJoinDate(new Date());
          result.setTitles(Lists.newArrayList("武将"));
          log.debug("addUser done");
          return result;
      }

      /**
      * 更新用户
      * @param user
      */
      public MUser updateUser(<strong>TUpdateUser</strong> user){
          MUser result=new MUser();
          result.setName("吕布");
          result.setAge(20);
          result.setGender("男");
          result.setId("1");
          result.setJoinDate(new Date());
          result.setTitles(Lists.newArrayList("武将"));
          log.debug("updateUser done");
          return result;
      }
  }
  </pre>
  <ul>
    <li>上例中的变更也是先定义.graphqls，然后在Mutation的java类里定义等价的方法</li>
  </ul>

  <h2>99.5 graphiql 查询工具</h2>
  <ul>
    <li>graphiql这是一个graphql-java自带的查询工具-graphiql，里面多了个i，访问地址 <a href="http://localhost:8080/graphiql">http://localhost:8080/graphiql</a></li>
    <li>graphiql相当于一个脚本的查询调试工具，可以确保查询脚本的正确性</li>
    <li>graphiql支持自动完成，所有的type和mutation都支持自动完成</li>
    <li>graphiql在右侧可以显示所有文档，配合文档可以快速更正查询和变更脚本中的错误</li>
  </ul>
  <h2>99.6 应用查询</h2>
  <pre>
  {
    user(usrName:"jim"){
      id age gender joinDate
    }
  }
  <strong>查询结果</strong>
  {
    "data": {
      "user": {
        "id": "1",
        "age": 30,
        "gender": "male",
        "joinDate": null
      }
    }
  }
  </pre>
  <ul>
    <li>查询字段可以任意添加，查询的结果可以即时的展示出来</li>
  </ul>

  <h2>99.7 应用变更</h2>
  <pre>
    <strong>//添加用户</strong>
  mutation {
    addUser(user: {
            name: "jim",
            gender: "男",
            age: 20}) {
      id
      name
    }
  }
  <strong>查询结果</strong>
  {
    "data": {
      "addUser": {
        "id": "1",
        "name": "吕布"
      }
    }
  }
  </pre>
  <ul>
    <li>在graphiql里执行命令，可以直接触发后端的对应的addUser方法</li>
    <li>在返回结果部分可以随意添加返回字段，可以很快显示结果</li>
  </ul>
</template>

<script>
export default {

}
</script>

<style lang='less' scoped>
@import url('../assets/common.less');

</style>