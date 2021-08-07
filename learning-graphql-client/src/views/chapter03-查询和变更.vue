<template>
  <div>
    <h1>第3章 查询和变量</h1>
    <p>本章阐述和查询GraphQL有关的知识</p>
    <p>注意：查询是并行执行的，变更是串行执行的</p>
    <pre>
    <strong>//完整的查询格式</strong>
    query 操作名 {
      返回关键字 {
        返回字段1
        返回字段2
        ....
        返回字段n
      }
    }
    <strong>//简写的查询格式</strong>
    {
      返回关键字 {
        返回字段1
        返回字段2
        ....
        返回字段n
      }
    }
    </pre>
    查询会返回
    <pre>
      {
        "data":{
          "返回关键字":{
            "返回字段1":"返回字段值1",
            "返回字段2":"返回字段值2",
            ....
            "返回字段n":"返回字段值n"
          }
        }
      }
    </pre>
    <ul>
      <li>查询的定义方式是一个类似json的语法，例如
        <pre>
    {
      muser { <strong>//定义的查询对象，对应后台的MUser类型，即 type MUser{...}</strong>
        name  <strong>//没有,分隔符</strong>
        gender
        age
      }
    }
        </pre>
        查询结果
        <pre>
    {
      "data":{ <strong>//顶级元素，固定的</strong>
        "muser":{ <strong>//和查询中的muser相同</strong>
          "name": "马超", <strong>//返回属性列表和查询中定义的格式是相同的</strong>
          "gender":"19",
          "age":20
        }
      }      
    }
        </pre>
      </li>
      <li>通过graphql提供的工具，比如java-graphql，或者nodejs实现的graphql服务器端都提供了实时查询结果展示</li>
    </ul>
    <h2>3.1. 字段（Fields）</h2>
    <ul>
      <li>字段类似强数据类型编程语言中类的属性</li>
    </ul>
    <pre>
    {
      user{
        name,
        titles
        jobs{
          jobName
          salary
        }
      }
    }
    </pre>
    返回数据
    <pre>
    {
      "data":{
        "user":{
          "name":"厨师甲",
          "title":["伙夫"],
          "jobs":[
            {
              "jobName":"厨师",
              "salary": 20000
            }
          ]
        }
      }
    }
    </pre>
    <h2>3.2. 参数（Arguments）</h2>
    <ul>
      <li>参数和强类型编程语言中的参数类似，但在GraphQL主要用在字段上</li>
    </ul>
    <pre>
    {
      user{
        name
        hello(<strong>name: String</strong>): String!
      }
    }
    </pre>
    <h2>3.3. 别名（Aliases）</h2>
    <ul>
      <li>如果需要给相同字段传入不同参数查询不同的数据的时候，别名就是用来解决重名问题的</li>
    </ul>
    <pre>
    {
      user1:findUser(name: String){ <strong>//定义了字段findUser的别名user1</strong>
        name
      }
      user2:findUser(name: String){ <strong>//定义了字段findUser的别名user2</strong>

      }
    }
    </pre>
    返回结果
    <pre>
    {
      "user1":{
        "name":"陈宫"
      },
      "user2":{
        "name":"吕布"
      }
    }
    </pre>
    <h2>3.4. 片段（Fragments）</h2>
    <ul>
      <li>片段是用来解决多个查询返回相同结果的复用问题</li>
      <li>通过...来引用片段</li>
    </ul>
    <pre>
      <strong>fragment FUser</strong> on MUser{
        name
        gender
        age
      }
      {
        user1: findUser(name: String){
          <strong>...FUser</strong>
        }
      }
    </pre>
    查询返回结果
    <pre>
      {
        "data":{
          "user1":{<strong>//返回数据和片段FUser1定义一致</strong>
            "name":"吕布",
            "gender":"男",
            "age":22
          }
        }
      }
    </pre>
    <h2>3.5. 操作类型（Operation Type）</h2>
    操作是告诉后端服务器GraphQL这段指令需要做什么事情

    <p>操作类型有三种</p>
    <ul>
      <li>query</li>
      <li>mutation</li>
      <li>subscription</li>
    </ul>
    <h2>3.6. 操作名称（Operation Name）</h2>
    <ul>
      <li>操作名称是定义在关键字query后的部分</li>
      <li>操作类型</li>
    </ul>
    定义格式
    <pre>
      query &lt;操作名&gt;{
        ....
      }
    </pre>
    例子如下
    <pre>
      query getUser {
        user{
          name
          gender
          age
        }
      }
    </pre>
    返回结果
    <pre>
      {
        "data":{
          "user":{
            "name":"吕布",
            "gender":"男",
            "age":20
          }
        }
      }
    </pre>
    <h2>3.7. 变量（Variables）</h2>
    <ul>
      <li>变量字段用来解决运行时传入动态类型的问题，比如在运行时可以传入多个类的继承类型，可以用变量来解决类型兼容问题</li>
      <li>变量通过$variable的格式来替换之前定义的类型，之前是val : String，现在是 $valType: String，其中的传参可以传{"varType":"String"}，两种方式是等价的</li>
      <li>声明的变量必须都是标量类型（基本类型），枚举或者输入对象类型</li>
      <li>声明变量如果要传入一个复杂对象类型，必须明确匹配服务器上定义的对应类型</li>
      <li>变量支持默认值，类似其他编程语言中的默认值的定义方式(value: String='foo')</li>
    </ul>
    <pre>
      query getUserImage($type: String){
        loadImage(type : $type){
          title,
          url
        }
      }
      //传参
      {"type":"application/png"}
    </pre>

    <h2>3.8. 指令（Directives）</h2>
    <ul>
      <li>指令类似vue中的v-if条件，可以根据测试条件来决定表达式的有效性，非常适用于根据传入的参数值来控制字段数量的场景</li>
      <li>目前graphql支持i的指令有@include和@skip
        <ul>
          <li>@include(if:boolean)：当if条件是true时才包含此字段</li>
          <li>@skip(if:boolean)：当if条件是true时跳过此字段</li>
          <li>指令也同样适用于服务器端</li>
        </ul>
      </li>
    </ul>
    <pre>
      query getUser($flag : boolean=true){
        name,
        jobs @include(if: $flag)
      }
      <strong>//当flag=true时会返回jobs，否则就不返回</strong>
    </pre>
    <h2>3.9. 变更（Mutations）</h2>
    <ul>
      <li>变更是用于处理数据修改的场景</li>
      <li>变更就相当于定义了一个方法，会自动映射到服务器端对应的方法并自动执行</li>
      <li>变更是串行执行的 <strong class="red">js单线程异步执行的，对于graphql服务器端程序来说，是否会存在并行执行的问题？</strong></li>
    </ul>
    <pre>
      mutation modifyUserEpisode($userType : String){
        modifyUser(user: $userType){
          name
          gender
          age
        }
      }
      <strong>//传入参数信息</strong>
      {
        "userType":{
          "name":"马超",
          "age":20,
          "gender":"男"
        }
      }
    </pre>
    <h2>3.10.内联片段（Inline Fragments）</h2>
    <ul>
      <li>内联片段的用法和片段（fragments）相似，但没有声明片段的名字，有点像是匿名的片段</li>
      <li>内联片段中也可以使用变量</li>
      <li>内联片段中也可以使用指令</li>
    </ul>
    <pre>
      query findUserEpisode($userType:Animal){
        find1: findUser(user: $usertype){
          name
          gender
          age
          ... on Cat{
            weight
          }
          .. on Human{
            salary
            joinDate
          }
        }
      }
    </pre>
    上面的例子解析如下
    <ul>
      <li>根据参数$userType的类型（必须是Animal的子类）</li>
      <li>如果$userType是Cat，那么返回的字段就是name, gender, age, weight</li>
      <li>如果$userType是Human，那么返回的字段就是name, gender, age, salary, joinDate</li>
    </ul>
    <h3>3.10.1.元字段（Meta fields）</h3>
    <ul>
      <li>如果不知道将在GraphQL服务器获得的数据类型，这时候就需要通过元字段来接收/存储对象的类型</li>
      <li>元字段的名字约定为__typename</li>
      <li>元字段 __typename 有点像是和服务器端约定的固有字段，不论如何服务器都会返回，如果客户端定义了__typename就赋值，否则就跳过</li>
      <li>GraphQL服务器会提供不少的元字段，其余的元字段较多用于内省（其它编程语言叫做反射/自省/RTTI等）</li>
    </ul>
    <pre>
      query{
        search(text: 'im'){
          __typename
          name
          gender
          age
          ... on Human{
            salary
            joinDate
          }
          ... on Cat{
            weight
          }
        }
      }
    </pre>

  </div>
</template>

<script>
export default {

}
</script>

<style lang="less" scoped>
@import url('../assets/common.less');

</style>