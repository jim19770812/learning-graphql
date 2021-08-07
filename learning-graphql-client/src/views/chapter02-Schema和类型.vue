<template>
  <div>
    <h1>第2章 Schema和类型</h1>
    <ul>
      <li>GraphQL是强数据类型</li>
      <li>GraphQL客户端层面一般会提供校验机制，可以在编译阶段执行语法检查</li>

    </ul>
    <h2>2.1. 类型系统（Type System）</h2>
    <ul>
      <li>GraphQL的查询语言基本上就是后端GraphQL定义对象的映射
        需要查询
        <pre>
    {
        <strong>abc</strong>{
        <strong class="yellow">name</strong>,
        <strong class="yellow">age</strong>
      }
    }
        </pre>
        会返回
        <pre>
    {
        <strong>"abc"</strong>:{
        <strong class="yellow">"name"</strong>:"xx",
        <strong class="yellow">"age"</strong>:20
      }
    }
        </pre>
      </li>
      <li>客户端可以根据定义大致知道返回数据的样子，这点很有意义</li>
    </ul>
    <h2>2.2. 类型语言（Type Language）</h2>
    <ul>
      <li>GraphQL是规范，可以由任何语言来实现，主流编程语言上都有GraphQL的实现</li>
      <li>GraphQL的查询语言称为GraphQL schema language</li>
    </ul>
    <h2>2.3. 对象类型和字段（Object Types and Fields）</h2>
    <ul>
      <li>对象类型是以关键字type定义的类型，类似java中的类</li>
      <li>字段支持传参，风格类似语法严谨的强类型语言，如java</li>
      <li>字段的参数支持默认值</li>
    </ul>
    <pre>
    <strong class="green">//下面定义了一个MUser的对象类型</strong>
    type MUser{
      id: String! <strong class="green">//!表示非空</strong>
      name: String!
      gender: String
      age : Int
      titles: [String!]! <strong class="green">//定义了包含非空字符串元素的非空数组</strong>
      <strong>job(type: Integer=1) : Job</strong><strong class="green">//定义了一个job字段，有一个type参数，默认值是1，注意这个job是字段而不是方法/函数</strong>
    }
    </pre>
    <h2>2.4. 查询和类型变更（The Query and Mutation Types）</h2>
    <pre>
    <strong>//查询命令的格式</strong>
    &lt;Schema&gt;{
      query: &lt;Query&gt;  <strong>//查询命令</strong>
      &lt;mutation&gt;: &lt;Mutation&gt; <strong>//修改类型，可选</strong>
    }
    </pre>
    例如有如下的查询
    <pre>
    muser{
      name,
      hello("马超")
    }
    </pre>
    这个查询命令对应服务器上的graphql定义
    <pre>
    type MUser{
      name: string
      hello(userName : String) : String
    }
    </pre>
    <ul>
      <li>每个GraphQL服务都有一个query类型，可能有一个mutation类型</li>
    </ul>
    <h2>2.5. 标量类型（Scalar Types）</h2>
    标量类型就是一般意义上的基础类型
    <ul>
      <li>ID：唯一ID，通常用以重新获取对象或者作为缓存中的键，ID类型和String一样的方式序列化，ID类型一般是无意义的字符串</li>
      <li>String：utf-8字符串</li>
      <li>Int：32位有符号整数</li>
      <li>Float：双精度浮点数</li>
      <li>Boolean：可选true/false</li>
    </ul>
    对于其余的类型，比如 <strong>Date</strong>，一般都是用自定义类型来解决，用字符串或者Int，并且不同的客户端实现上可能都会有实现。
    <h2>2.6. 枚举类型（Enumeration Types）</h2>
    枚举类型和其它编程语言中的枚举类型相同
    <ul>
      <li>强制要求值必须是枚举值中的一个</li>
      <li>强数据类型，提供值的验证和检查机制</li>
      <li>枚举类型在不同语言的实现中都有不同的枚举处理模式，比如js中枚举会映射成整数类型</li>
    </ul>
    <pre>
    enum Gender{ <strong>//性别枚举</strong>
      MALE,
      FMALE
    }
    </pre>
    <h2>2.7. 列表和非空（Lists and Non-Null）</h2>
    列表类型类似java的List类型，可以用来包装其它标量类型（基础类型）
    <pre>
    type MUserList{
      users: [MUser]! <strong>//用户对象列表，非空</strong>
      findUser(userName : String!) :MUser! <strong>//注意userName也是非空</strong>
    }
    </pre>
    <ul>
      <li>如果服务器端在返回users时并没有数据，那么服务器会返回一个执行错误，便于客户端处理</li>
      <li>如果字段的参数标记了非空，如果传入空，会导致服务器会返回一个验证错误</li>
    </ul>
    为了演示数组和null的规则，提供一组例子来阐述。假设有一个类型包含数组field1，类型定义如下
    <pre>
    type Array1{
      fiel1: [String!]
    }
    </pre>
    赋值代码
    <pre>
      array1.field1: null <strong>//有效</strong>
      array1.field1: []  <strong>//有效</strong>
      array1.field1: ['a', 'b'] <strong>//有效</strong>
      array1.field1: ['a', 'b', <span class="red">null</span>] <strong>//错误</strong>
    </pre>

    <h2>2.8. 接口（Interfaces）</h2>
    <ul>
      <li>接口类型和其它强类型编程语言中的接口功能类似，是一个抽象类型，可以包含字段，对象必须实现这些字段</li>
    </ul>
    <pre>
      interface InterfaceUser1{ <strong>//定义接口的方式和强类型编程语言相同</strong>
        id: ID!
        name: String!
        gender : Gender!
        age : Int!
      }

      type MUser1 implements InterfaceUser1{ <strong>//连implements关键字都和强类型编程语言一样</strong>
        id: ID!
        name: String!
        gender : Gender!
        age : Int!
        hello(name : String): String
      }
    </pre>
    <h2>2.9.联合类型（Union Types）</h2>
    <ul>
      <li>联合类型有点像是typescript里的 |，连指令都是相同的</li>
    </ul>
    <pre>
    type Type1{
      value1: Int
    }
    type Type2{
      value2: Int
    }
    union UnionType1 = Type1 | Type2 <strong>//联合类型，UnionType1联合了两个类型Type1和Type2</strong>
    </pre>
    <h2>2.10.输入类型（Input Types）</h2>
    <ul>
      <li>输入类型就是把类型作为参数传入字段的方法，有点像是java中的Class&lt;T&gt;</li>
      <li>输入类型使用关键字 input 定义</li>
      <li>输入类型既可以使用标量类型（基础类型）、枚举类型、自定义类型（对象）</li>
      <li>在处理mutation时自定义对象类型很有用</li>
      <li>输入对象类型的字段不能有参数</li>
    </ul>
    <pre>
      intpu MyInputType1{ <strong>//定义了一个输入类型</strong>
        name: String!
        value1: Int!
      }
      mutation ModifyMUser($type1 : MyInputType1){ <strong>//应用了输入类型MyInputType1的参数$type1</strong>
        modify(value : $type1){ <strong>//应用参数$type1来定义参数value</strong>
          user: MUser!
        }
      }
    </pre>
    执行后返回结果：
    <pre>
    {
      "data":{
        "modify":{
          "user":{
            "id":"100",
            "name":"吕布",
            "gender":"MALE",
            "age": 20
          }
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