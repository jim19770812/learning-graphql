<template>
  <div>
    <h1>第1章 入门</h1>
    <ul>
      <li>graphql是种API查询语言，强类型</li>
      <li>graphql是规范，不同语言上大都有对应本语言的graphql实现</li>
      <li>graphql客户端需要对应graphql服务器，调用服务器上的查询实现在客户端上展示结果</li>
      <li>当后端收到查询请求时，会进行验证和执行</li>
    </ul>
    <div>
      后端schem定义如下
      <pre>
        type Query{ <strong>//Query是必须要有的，貌似这是类型定义的入口，而且要区分大小写</strong>
          abc():<strong>User</strong>
        }

        type <strong>User</strong>{
          user_id : String!   <strong>//!表示字段非空</strong>
          user_name: String!
          user_gender: String
          user_age : Int
        }
      </pre>
      前端查询命令定义如下
      <pre>
        {
          abc(){
            user_id, user_name, user_gender, user_age <strong>//也可以省略掉逗号</strong>
          }
        }

      </pre>
      经过后端执行后，会返回
      <pre>
        {
          "data":{ <strong>//返回数据一定以data为根节点</strong>
            "abc":{ <strong>//abc是方法的名字</strong>
              "user_id":"100", <strong>//类型和数量与User保持一致</strong>
              "user_name":"张飞",
              "user_gender":"男",
              "user_age": 20
            }
          }
        }
      </pre>
    </div>
  </div>
</template>

<script>
export default {

}
</script>

<style lang='less' scoped>
@import url('../assets/common.less');

</style>