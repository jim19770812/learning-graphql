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
        &lt;groupId&gt;com.graphql-java&lt;/groupId&gt;
        &lt;artifactId&gt;<strong>graphql-java-tools</strong>&lt;/artifactId&gt;
        &lt;version&gt;5.2.4&lt;/version&gt;
    &lt;/dependency&gt;
    &lt;dependency&gt;
        &lt;groupId&gt;com.graphql-java&lt;/groupId&gt;
        &lt;artifactId&gt;<strong>graphql-spring-boot-starter</strong>&lt;/artifactId&gt;
        &lt;version&gt;5.0.2&lt;/version&gt;
    &lt;/dependency&gt;
    &lt;dependency&gt;
        &lt;groupId&gt;com.graphql-java&lt;/groupId&gt; <strong>//这个是iql工具</strong>
        &lt;artifactId&gt;<strong>graphiql-spring-boot-starter</strong>&lt;/artifactId&gt;
        &lt;version&gt;5.0.2&lt;/version&gt;
    &lt;/dependency&gt;
    </pre>
    <ul>
      <li>graphql-java-extended-scalars这是一个GraphQL的scalar的扩展库，里面有DateTime和Date类型的处理，但貌似不如自己写的好用，里面还有一些其它的标量类，如果有需要可以在里面找找</li>
    </ul>

    <h2>99.2 后端实现</h2>
    <ul>
      <li>配置文件默认在/resources/graphql下，扩展名必须是 .graphqls</li>
      <li>这两个是基础的类型，其中MUser和Job是一对多的</li>
      <ul>在*.graphqls文件中#打头的是注释，注释会自动在前端的graphiql里以文档的形式显示，很方便</ul>
    </ul>
    <h3>99.2.1 类型定义</h3>
    <p>MUser类型定义</p>
    <pre>
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
    </pre>
    <p>Job类型定义</p>
    <pre>
  #工作
  type Job {
    #工作名称
    name : String!
    #薪水
    salary : Float
  }
    </pre>

  <h3>99.2.2 GraphQL类型的后端Java代码映射</h3>
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
  </pre>
  <h3>99.2.3 MUserResolver.java</h3>
  <ul>
    <li>MUser的Resolver，Resolver和Entity组合起来实现Schema所定义的功能，Entity定义纯粹的属性，Resolver实现属性的getter方法</li>
  </ul>
  <pre>
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
  </pre>
  <ul>
    <li>MUser中有个job(name: String): Job的方法定义，这是一个带参数的字段</li>
    <li>由MUserResolver负责解析，注意MUserResolver.getJob方法中的地一个参数是属主对象MUser的实例</li>
    <li>MUserResolver的方法前面有个get，说明这是个getter方法，只有这样才能被识别</li>
    <ul>Muser+MuserResolver联合起来实现了GraphQL总定义的类型MUser</ul>
  </ul>

  <h3>99.2.4 Job.java</h3>
  Job是类型Job的映射
  <pre>
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

  <h3>99.2.5 类型Query的定义</h3>
  <ul>
    <li>Query类型相当与Graphql的查询入口，所有的查询功能都要在这里定义</li>
    <li>Query只能定义一次，并且名字是固定的</li>
    <li>GraphQL类型String[]要映射成List&lt;String&gt;，不能映射成String[]，这点要注意</li>
    <li>/resources/graphql/root.graphqls</li>
  </ul>
  <pre>
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
    </pre>
    <p/>
    <h3>99.2.6 QueryResolver.java</h3>
    <ul>
      <li>注意QueryResolver中的定义死type Query中方法的映射</li>
      <li>和type映射一样，String[]要映射成List&lt;String&gt;</li>
    </ul>
    <pre>
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
  </div>

  <h2>99.3 Mutations定义</h2>
  <ul>
    <li>Muations是用于处理变更的</li>
    <li>Muations和Query一样，只能定义一次</li>
  </ul>
  <h3>Input的定义</h3>
  <ul>
    <li>定义的文件/resources/graphqls/root.graphqls</li>
  </ul>
  <h3>99.3.1 TAddUser</h3>
  <pre>
  #添加用户
  input <strong>TAddUser</strong>{
      #用户名
      name: String!
      #性别
      gender: String!
      #年龄
      age: Int!
  }
  </pre>
  TAddUser.java
  <pre>
  <strong>TAddUser.java</strong>
  @Getter @Setter
  @ToString
  @EqualsAndHashCode
  public class <strong>TAddUser</strong> {
      private String name;
      private String gender;
      private Integer age;    
  }
  </pre>
  <h3>99.3.2 TUpdateUser</h3>
  <pre>
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
  </pre>
  TUpdateUser.java
  <pre>
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
  </pre>
  <h3>99.3.4 Mutation定义</h3>
  <ul>
    <li>定义的文件/resources/graphqls/root.graphqls</li>
  </ul>
  <pre>
  #变更
  type Mutation{
      #添加用户
      addUser(user: TAddUser): MUser!
      updateUser(user: TUpdateUser!): MUser!
  }
  </pre>

  Mutation.java
  <pre>
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

  <h2>99.4 graphiql 查询工具</h2>
  <ul>
    <li>graphiql这是一个graphql-java自带的查询工具-graphiql，里面多了个i，访问地址 <a href="http://localhost:8080/graphiql">http://localhost:8080/graphiql</a></li>
    <li>graphiql相当于一个脚本的查询调试工具，可以确保查询脚本的正确性</li>
    <li>graphiql支持自动完成，所有的type和mutation都支持自动完成</li>
    <li>graphiql在右侧可以显示所有文档，配合文档可以快速更正查询和变更脚本中的错误</li>
  </ul>
  <h2>99.5 应用查询</h2>
  <h3>99.5.1 一般的查询</h3>
  <pre>
  <strong>查询命令</strong>
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
  <h3>99.5.2 应用参数查询</h3>
  查询命令
  <pre>
  query(<strong>$name</strong>: String!){
    user(usrName: $name){
      id, name, gender, joinDate
    }
  }
  <strong>参数</strong>
  {
    <strong>"name"</strong>:"jim"
  }
  </pre>
  查询结果
  <pre>
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
  <h2>99.6 应用变更</h2>
  <h3>99.6.1 无参数的变更</h3>
  添加用户
  <pre>
  mutation {
    addUser(user: {
            name: "jim",
            gender: "男",
            age: 20}) {
      id
      name
    }
  }
  </pre>
  查询结果
  <pre>
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
  <h3>99.6.2 带参数的变更</h3>
  <pre>
  mutation(<strong>$name</strong>: String!){
    addUser(user:{
      name:$name,
      gender:"男",
      age: 20
    }) {
      id, name, gender, joinDate
    }
  }

  <pre>参数</pre>
  {
    <strong>"name"</strong>:"jim"
  }
  </pre>
  查询结果
  <pre>
  {
    "data": {
      "addUser": {
        "id": "1",
        "name": "吕布",
        "gender": "男",
        "joinDate": "Sat Sep 04 15:57:06 CST 2021"
      }
    }
  }
  </pre>
  <ul>
    <li>注意返回的日期格式并不是标准的日期格式，需要在后端添加对DateTime的标量类才可以，有扩展库可以实现，但扩展库并不好使，还是自己实现更方便，后面会提供这样的例子</li>
  </ul>
  <h3>99.6.3 自定义日期时间标量</h3>
  <ul>
    <li>默认的GraphQL只是提供了基本类型的标量，对于日期时间是不提供的，但GraphQL提供了扩展标量机制可以让我们方便的实现自定义类型</li>
  </ul>
  实现标量类
  <pre>
  @Component
  public class DateTimeScalarType extends GraphQLScalarType {
      private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  
      public DateTimeScalarType() {
          <strong>//这里定义了类型DateTime和需要转换的两个java类型</strong>
          super("DateTime", "日期时间标量", new Coercing&lt;Date, String&gt;() {
              @Override
              @SneakyThrows
              public String serialize(Object o) {
                  Date date = (Date) o;
                  return DT.ofDate(date).format(DATE_TIME_FORMAT);
              }
  
              @Override
              @SneakyThrows
              public Date parseValue(Object o) {
                  String value = String.valueOf(o);
                  if ("null".equalsIgnoreCase(value) || "".equalsIgnoreCase(value)) {
                      return null;
                  }
                  Date result=DT.of_yyyy_MM_dd_HH_mm_ss(value).orElseThrow(()->new RuntimeException(value+" 日期格式转换失败")).date();
                  return result;
              }
  
              @Override
              @SneakyThrows
              public Date parseLiteral(Object o) {
                  String value = String.valueOf(o);
                  if ("null".equalsIgnoreCase(value) || "".equalsIgnoreCase(value)) {
                      return null;
                  }
                  Date result= DT.of_yyyy_MM_dd_HH_mm_ss(value).orElseThrow(()->new RuntimeException(value+"日期格式转换失败")).date();
                  return result;
              }
          });
      }
  }
  </pre>
  需要在定义类型的.graphqls文件中声明DateTime类型
  <pre>
    scalar DateTime <strong>#这句话加在文件的头部或者尾部都可以</strong>
  </pre>
  之后就可以正常的使用自定义的标量DateTime了
  <h2>99.7 前端调用GraphQL的例子</h2>

  <h3>99.7.1 前端调用查询</h3>
  <h4>99.7.1.1 无参数查询</h4>
  <div>
    <button @click="btn99_7_1_query1">调用服务器查询的例子</button>
    <div>查询结果：{{ state.out99_7_1_1 }}</div>
  </div>

  <h4>99.7.1.2 带参数的查询</h4>
  <div>
    <button @click="btn99_7_1_query2">调用服务器查询的例子</button>
    <div>查询结果：{{state.out99_7_1_2}}</div>
  </div>

  <h3>99.7.2 前端调用变更</h3>
  <div>
    <button @click="btn99_7_2_addUser">调用服务器查询的例子</button>
    <div>查询结果：{{state.out99_7_3}}</div>
  </div>
</template>

<script>
import { reactive } from 'vue'
import axios from '_axios@0.21.1@axios';
/* eslint-disable @typescript-eslint/no-empty-function */
export default {
  name: 'chapter99',
  components:{
  },
  // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
  setup() {
    console.log("log");
    const state = reactive({
      out99_7_1_1:'',
      out99_7_1_2:'',
      out99_7_3:''
    })
    /**
     * 不带参数查询
     */
    const btn99_7_1_query1 = () => {
      console.log("btn99_7_1_query1")
      let userName="jim"
      axios.post('/graphql', { //下面的传参方式有sql注入风险，所以要慎重使用，不过如果后台不是基于SQL的RDBMS，那貌似是可以用的 :)
          query: `
              {
                user(usrName:"${userName}"){
                  id age gender joinDate
                }
              }
            `
        }).then((result) => {
        console.log(result.data)
        state.out99_7_1_1=result.data
        console.log("data:", JSON.stringify(result.data))
      });
    }
    /**
     * 带参数查询
     */
    const btn99_7_1_query2 = () => {
      console.log("btn99_7_1_query2")
      let userName="jim"
      let data ={
        query:`
            query($name: String!){
              user(usrName: $name){
                id, name, gender, joinDate
              }
            }
        `,
        variables:{
          "name": userName
        }
      }
      axios.post('/graphql', data).then((result) => {
        console.log(result.data)
        state.out99_7_1_2=result.data
        console.log("data:", JSON.stringify(result.data))
      });
    }
    /**
     * 执行变更
     */
    const btn99_7_2_addUser=()=>{
      console.log("btn99_7_2_addUser")
    }
    return {
      state,
      btn99_7_1_query1,
      btn99_7_1_query2,
      btn99_7_2_addUser

    }

  }
}
</script>

<style lang='less' scoped>
@import url('../assets/common.less');

</style>