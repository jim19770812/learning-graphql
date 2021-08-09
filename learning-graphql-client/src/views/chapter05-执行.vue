<template>
  <div>
    <h1>第5章 执行</h1>
    <ul>
      <li>执行的结果一般是json格式，也支持别的格式比如yaml格式</li>
    </ul>
    本章例子的定义：后端部分
    <pre>
    type Query{ <strong>//Query是根字段，也是所有查询的入口点</strong>
      human(id: ID!): Human
    }
    type Human{
      name: String!
      gender: String!
      age : Int!
      Job : [Job!]
    }
    type Job{
      name: String!
      salary: Float
    }
    </pre>
    本章例子的定义：前端查询
    <pre>
    {
      human(id: 1002){ <strong>//查询id=1002的人</strong>
        name
        jobs{ <strong>//多层级</strong>
          name
        }
      }
    }
    </pre>
    上述例子的解析
    <ul>
      <li>
        <div class="green">根字段&amp;解析器</div>
        <ul>
          <li>后端API的入口点Query，注意不同的实现语言的实现逻辑有差异
            <pre>
              <strong>//基于js的实现</strong>
              Query:{
                human(obj, args, context, info){
                  return context.db.loadHumanById(args.id).then(userData=>new Human(userData))
                }
              }
            </pre>
            其中的参数字段的解释如下
            <ul>
              <li>obj：上一级对象</li>
              <li>args：可以提供在GraphQL查询中传入的参数</li>
              <li>context：会被提供给所有解析器，并且保存上下文访问信息</li>
              <li>info：保存和当前查询有关的字段信息和schema详细信息的值</li>
            </ul>
          </li>
          <li>返回的数据四个Human类型，可以通过这个类型约束返回的字段</li>
        </ul>
      </li>
      <li>
        <div class="green">标量强制</div>
        标量就是基本类型，标量限制就是基本类型限制
      </li>
      <li>
        <div class="green">列表解析器</div>
        <pre>
    {
      human(id: 1002){
        name
        jobs{
          name
        }
      }
    }
    <strong>//因为返回的jobs是个list，所以返回的数据就是个列表</strong>
        </pre>
      </li>
      <li>
        <div class="green">产生结果</div>
      </li>
    </ul>
  </div>
</template>

<script>
export default {

}
</script>

<style lang="less" scoped>
@import url('../assets/common.less');

</style>