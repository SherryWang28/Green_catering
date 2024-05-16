<html>
<#include "../zujian/top.ftlh">

<body>
<div id="wrapper" class="toggled">
    <#--内容区域头部样式-->
    <h2 class="bg-info text-white p-3  text-center">预定餐品管理</h2>
    <#--左边栏-->
    <#include "../zujian/left.ftlh">


    <#--主要内容content-->


    <div id="page-content-wrapper">
        <button class="btn btn-warning text-white my-3 offset-md-1">
            预定餐品管理管理
        </button>
        <div class="row clearfix">
            <div class="col-md-10 offset-md-1 column">
                <table class="table table-bordered table-hover">
                    <thead>
                    <tr class="table-info">
                        <th>客户订单</th>
                        <th>用户姓名</th>
                        <th>手机号/微信</th>
                        <th> 订单金额</th>
                        <th>预定时间</th>
                        <th>菜品</th>
                        <th>菜品处理</th>
                        <th>（订单状态）接受/拒绝</th>
                        <th> 创建时间 </th>
                        <th colspan="1">操作</th>
                    </tr>
                    </thead>
                    <tbody>

                    <#list listSmall as category>
                        <tr>
                            <td>${category.num}</td>
                            <td>${category.createTime}</td>
                            <td>${category.num}</td>
                            <td>${category.createTime}</td>
                            <td>${category.num}</td>
                            <td>${category.createTime}</td>
                            <td>${category.num}</td>
                            <td>${category.num}</td>
                            <td>${category.createTime}</td>
                            <td><a href="/diancan/adminPaihao/ruzhuo?id=${category.id}">可入座</a></td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>

    </div>


</div>
</body>
</html>