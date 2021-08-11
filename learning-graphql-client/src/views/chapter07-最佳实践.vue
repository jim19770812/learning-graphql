<template>
  <div>
    <h1>第7章 最佳实践</h1>
    GraphQL忽视了部分特性，例如网络处理，授权，分页，虽然这些特性并不是GraphQL规范的一部分，但仍然是有解决方案的

    <h2>7.1.HTTP</h2>
    一般情况下GraphQL只支持单一Http端口和一个url请求地址，基本用不上需要暴露多个地址和多个url请求地址的场景。

    <h3>网络请求管道</h3>
    GraphQL的请求应该在身份验证通过之后发起

    <h3>URI和路由</h3>
    <ul>
      <li>一般来说GraphQL的请求入口点都是 /graphql，所有的请求都要通过这个入口点获得数据</li>
    </ul>

    <h3>Http方法、标题和正文</h3>
    <h4>GET请求</h4>
    <pre>
      //有查询
      {
        find{
          name
        }
      }
    </pre>
    <pre>
      curl -XGET http://localhost:8080/myapp-server/graphql?query={find{name}}
    </pre>

    <h4>POST请求</h4>
    GraphQL请求一般都要带请求头 Content-Type: application/json 来发起
    <pre>
      {
        "query":"...",
        "operationName":"...",
        "variables":{"name":"jim"}
      }
      <strong>//其中operationName和variables是可选的，只有存在多个操作时才需要</strong>
    </pre>
    <ul>
      <li>如果存在 query 查询参数，那么就会以GET的方式进行解析和处理</li>
      <li>如果带请求头 Content-Type: application/json，就会把POST的内容视作GraphQL的查询字符串</li>
    </ul>

    <h4>响应</h4>
    <ul>
      <li>返回数据是json格式</li>
      <li>只有执行成功，响应中才会包含data字段</li>
      <li>只有执行错误，响应中才会包含errors字段</li>
    </ul>

    <h3>生产环境中要禁用GraphQL的调试工具</h3>
    <pre>
    app.use('/graphql', graphqlHTTP({
      schema: MySessionAwareGraphQLSchema,
      graphiql: process.env.NODE_ENV === 'development',
    }));
    </pre>
    <h3>使用Node.js推荐的GraphQL框架</h3>
    <ul>
      <li>express-graphql</li>
      <li>apollo-server</li>
    </ul>

    <h2>7.2.授权</h2>
    <ul>
      <li>授权的意图是将逻辑委托给业务逻辑层，描述用户、会话、上下文是否具有执行操作的权限或者是否具有查看某条数据的权限</li>
      <li>一般来说，前端的授权并不安全，务必从后端处理，前端只要处理一些和交互有关的权限</li>
      <li>只有一些在浏览器端缓存到localstorage里的数据会需要用到授权机制</li>
    </ul>

    <h2>7.3.json(使用gzip压缩)</h2>
    相应头里增加 Accept-Encoding: gzip 可能会让通讯延迟更小一点。

    <h2>7.4.版本控制</h2>
    GraphQL没有约定版本控制机制，但GraphQL可以通过Schema的变化来模拟版本号控制功能

    <h2>7.5.可以为空的性质</h2>
    <ul>
      <li>在GraphQL中默认每个字段都可以是null</li>
      <li>GraphQL中提供了non-null的类型确保字段不会接收到来自服务器端返回的null值，如果服务器端返回了null，会抛出异常</li>
      <li>一定要对允许为空字段做判断和检查，避免意外返回的null造成程序出错</li>
    </ul>
    <h2>7.6.分页</h2>
    <ul>
      <li>分页通过接收参数来解决</li>
    </ul>
    分页的两种做法
    <h3>7.6.1.第一种做法：根据页号分页</h3>
    <ul>
      <li>相对来说这是比较传统的方法，对于mysql来说就是控制limit参数来实现分页，在较大数据量时性能会略差，对于oracle来说,startrow,endrow的做法性能不错</li>
    </ul>
    <h3>7.6.2.第二种做法：根据最后一条的ID分页</h3>
    <ul>
      <li>相对来说，在mysql场景中，这种做法性能优于根据limit来分页的做法，但实现脚本比较麻烦，对于oracle来说可能这样做要比startrow,endrow要慢</li>
    </ul>
    <h2>7.7.全局对象识别</h2>
    一致的对象缓存实现了简单的缓存和对象查找
    <pre>
    {node(id: "4") {
      id
      ... on User {
        name
      }
    }
    </pre>
    上述例子中字段名是node，它返回的对象类型是Node，这貌似是一种约定，根据字段名获取字段首字母大写的对象，貌似这是一种规范

    <h2>7.8.服务器端的批处理与缓存</h2>
    <ul>
      <li>由于GraphQL每一种类型都有一个唯一的函数来解析该值，因为不优化过于简单的GraphQL可能会加大服务器调用次数，从而拉低了性能</li>
      <li>批处理技术可以实现合并多个请求，例如Facebook的<a href="https://github.com/graphql/dataloader">DataLoader</a>工具，它可以把多个请求打包成单个请求一起发往后端服务器</li>
      <li>Http2也可以实现类似功能，只不过它是通过合并请求实现的，两者做法不同但结果是差相近的</li>
    </ul>
  </div>
  <h2>7.9.关于GraphQL的思考</h2>
  <h3>7.9.1.一切皆是图</h3>
  <ul>
    <li>图是将真实世界现象变成模型的强大工具，可以用GraphQL通过定义Schema把业务模型构建成图</li>
    <li>客户端通过类似面向对象编程的方式进行查询和获取数据</li>
  </ul>
  <h3>7.9.2.共同语言</h3>
  <ul>
    <li>接口的命名要保持见名知意</li>
  </ul>
  <h3>7.9.3.业务逻辑层</h3>
  <h3>7.9.4.一次一步</h3>
</template>

<script>
export default {

}
</script>

<style lang="less" scoped>
@import url('../assets/common.less');

</style>