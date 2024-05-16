<html>
<#include "../zujian/top.ftlh">

<body>
<div id="wrapper" class="toggled">
    <#--内容区域头部样式-->
    <h2 class="bg-info text-white p-3  text-center">商家账户删除</h2>
    <#--左边栏-->
    <#include "../zujian/Superleft.ftlh">
    <#--主要内容content-->
    <div id="page-content-wrapper" class="container">
        <div class="row clearfix">
            <div class="col-md-12 column">
                <form role="form" method="post" action="/diancan/Seller/save">
                    <div class="form-group">
                        <label>商家店铺名称</label>
                        <input name="sellername" type="text" class="form-control"
                               value="${(sellerInfo.sellername)!''}"/>
                    </div>
                    <div class="form-group">
                        <label>手机号/微信/商家编号</label>
                        <input name="phone" type="text" class="form-control"
                               value="${(sellerInfo.phone)!''}"/>
                    </div>
                    <div class="form-group">
                        <label>密码</label>
                        <input name="password" type="text" class="form-control"
                               value="${(sellerInfo.password)!''}"/>
                    </div>
                    <div class="form-group">
                        <label>商家权限管理设置</label>
                        <select name="sellerType" class="form-control">
                            <#list enums as val>
                                <option value="${val.getCode()}"
                                        <#if (sellerInfo.sellerType)?? && sellerInfo.sellerType == val.getCode()>
                                            selected
                                        </#if>
                                >${val.getMessage()}
                                </option>
                            </#list>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>商家商家资格审定</label>
                        <select name="quali" class="form-control">
                            <#list enums1 as val>
                                <option value="${val.getCode()}"
                                        <#if (sellerInfo.quali)?? && sellerInfo.quali == val.getCode()>
                                            selected
                                        </#if>
                                >${val.getMessage()}
                                </option>
                            </#list>
                        </select>
                    </div>




                    <input hidden type="text" name="sellerId"
                           value="${(sellerInfo.sellerId)!''}">
                    <button type="submit" class="btn  btn-info">提交</button>
                </form>
            </div>
        </div>
    </div>

</div>
</body>
</html>