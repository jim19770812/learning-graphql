<template>
  <div>
    <h1>第6章 内省</h1>
    <ul>
      <li>内省类似java的反射</li>
      <li>通过访问__schema对象来获取GraphQL的可用类型
        <code>
          console.log(__schema)
        </code>
        <pre>
  <strong>//所有的类型都会出现在这里</strong>
  {
    {"name":"Mutation"},
    {"name":"Episode"},
    {"name":"Human"}
  }
        </pre>
      </li>
      <li>以双下划线开头的都属于内省的一部分</li>
    </ul>

    几个关于内省的例子
    <pre>
    {
      __type(name: "Human"){
        name <strong>//类型名字</strong>
        kind <strong>//接口/类</strong>
        fields <strong>//包含的字段列表</strong>
      }
    }
    </pre>

    返回结果如下
    <pre>
    {"data":{
      "_type":{
        "name":"Human",
        "kind":"OBJECT",<strong>//如果是接口，这里会返回INTERFACE</strong>
        "fields":[{ <strong>//所有的字段列表</strong>
            "name":"id",
            "type":{
              "name":"String",
              "kind":"NON_NULL"
            }
          },{
            "name":"name",
            "type":{
              "name":"String",
              "kind":"NON_NULL"
            }
          },{
            "name":"gender",
            "type":{
              "name":"String",
              "kind":"NON_NULL"
            }
          },{
            "name":"age",
            "type":{
              "name":"Int",
              "kind":"NON_NULL"
            }
          }
          <strong>//如果有方法也会在这里列出</strong>
        ]
      }
    }}
    </pre>
    <ul>
      <li>内省在需要确认对象的类型和字段列表和类型的时候很有用</li>
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