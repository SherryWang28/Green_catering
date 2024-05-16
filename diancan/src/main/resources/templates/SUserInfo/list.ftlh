<html>
<#include "../zujian/top.ftlh">

<body>
<div id="wrapper" class="toggled">
    <#--内容区域头部样式-->
    <h2 class="bg-info text-white p-3  text-center">用户账户管理</h2>
    <#--左边栏-->
    <#include "../zujian/Superleft.ftlh">
    <#--主要内容content-->


            <div id="page-content-wrapper" class="container">
                <#--添加管理员-->
            <button class="btn btn-warning text-white my-3"
                    onclick="window.parent.location.href='/diancan/SUserInfo/index';">
                添加管理员
            </button>
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <table class="table table-bordered table-hover">
                        <thead>
                        <tr class="table-info">
                            <th>id</th>
                            <th>名字</th>

                            <th>手机号/微信</th>
                            <th>类型</th>

                            <th>修改时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>

                        <#list suserList as suserBean>
                            <tr>
                                <td>${suserBean.openid}</td>
                                <td>${suserBean.susername}</td>
                                  <td>${suserBean.phone}</td>
                                <td>顾客</td>

                                <td>${suserBean.updateTime}</td>
                                <td><a href="/diancan/SUserInfo/index?openid=${suserBean.openid}">修改</a></td>
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