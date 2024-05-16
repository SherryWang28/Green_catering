<html>
<#include "../zujian/top.ftlh">

<body>
<div id="wrapper" class="toggled">
    <#--内容区域头部样式-->
    <h2 class="bg-info text-white p-3  text-center">超级管理员修改/添加</h2>
    <#--左边栏-->
    <#include "../zujian/left.ftlh">
    <#--主要内容content-->
    <div id="page-content-wrapper" class="container">
        <div class="row clearfix">
            <div class="col-md-12 column">
                <form role="form" method="post" action="/diancan/SUserInfo/save">
                    <div class="form-group">
                        <label>名字</label>
                        <input name="susername" type="text" class="form-control"
                               value="${(suserInfo.susername)!''}"/>
                    </div>
                    <div class="form-group">
                        <label>手机号/微信</label>
                        <input name="phone" type="text" class="form-control"
                               value="${(suserInfo.phone)!''}"/>
                    </div>

                <!--    <div class="form-group">
                        <label>管理员权限设置</label>
                        <select name="suserType" class="form-control">

                        </select>
                    </div>
                    -->
                    <input hidden type="text" name="openid"
                           value="${(suserInfo.openid)!''}">
                    <button type="submit" class="btn  btn-info">提交</button>
                </form>
            </div>
        </div>
    </div>

</div>
</body>
</html>