<html>
<#include "../zujian/top.ftlh">
<body>
<div id="wrapper" class="toggled">
    <#--内容区域头部样式-->
    <h4 class="bg-info text-white p-1  text-center">顾客订单管理</h4>
    <#--左边栏-->
    <#include "../zujian/left.ftlh">
    <div id="page-content-wrapper" class="container-fluid row clearfix">
        <#--批量导出菜品订单-->
        <button class="btn btn-info text-white m-3" onclick="window.parent.location.href='/diancan/Green/export';">
            导出订单到excel
        </button>
        <#--显示定制化餐品订单-->
        <input class="btn btn-info text-white m-3" value="显示定制化餐品" onclick="showItemsWithCustom('是')">
        <script>
            function showItemsWithCustom(customValue) {
                // 获取表格的tbody元素
                var tbody = document.querySelector('.table-striped tbody');

                // 遍历tbody中的每一行
                for (var i = 0; i < tbody.rows.length; i++) {
                    var row = tbody.rows[i];

                    // 获取“定制化餐品”的单元格
                    var customCell = row.cells[3]; // 假设“定制化餐品”是第一列

                    // 检查单元格内容是否匹配筛选条件
                    var isMatch = customCell.textContent.trim() === customValue;

                    // 根据匹配结果设置行的显示状态
                    row.style.display = isMatch ? '' : 'none'; // 匹配则显示，否则隐藏
                }
            }
        </script>




        <#--订单表格-->


        <div class="col-md-12 column">
            <table class="table table- table-striped table-bordered table-sm ">
                <thead>
                <tr class="table-info">
                    <th>订单id</th>
                    <th>用户姓名</th>
                    <th>手机号/微信</th>
                    <th>定制化餐品</th>
                    <th>口味</th>
                    <th>预定时间</th>
                    <th>备注</th>
                    <th>订单金额</th>
                    <th>订单状态</th>
                    <th>被催单次数</th>
                    <th>创建时间</th>

                    <th colspan="2">操作</th>



                </tr>
                </thead>
                <tbody>
                <#list orderDTOPage.getContent() as orderDTO>

                    <tr>
                        <td>${orderDTO.orderId!''}</td>
                        <td>${orderDTO.buyerName!''}</td>
                        <td>${orderDTO.buyerPhone!''}</td>

                        <td>${orderDTO.buyerGreen!''}</td>
                        <td>${orderDTO.buyerTaste!''}</td>
                        <td>${orderDTO.buyerDateTime!''}</td>
                        <td>${orderDTO.buyerRemarks!''}</td>
                        <td>${orderDTO.orderAmount!''}</td>
                        <td>${orderDTO.getOrderStatusEnum().message}</td>
                        <td>${orderDTO.cuidan!'0'}</td>
                        <td>${orderDTO.createTime}</td>




                        <td><a href="/diancan/Green/detail?orderId=${orderDTO.orderId!''}">详情</a></td>
                        <td>
                            <#if orderDTO.getOrderStatus()==2 ><#--只有取消的订单才可以删除-->
                                <a href="/diancan/Green/remove?orderId=${orderDTO.orderId!''}">删除</a>
                            <#else >
                                <a href="/diancan/Green/cancel?orderId=${orderDTO.orderId!''}">取消</a>
                            </#if>

                        </td>
                    </tr>


                </#list>





                </tbody>
            </table>
        </div>

        <#--底部分页-->
        <div class="col-md-12 column">
            <ul class="pagination ">
                <#if currentPage lte 1>
                    <li class="page-item disabled"><a class="page-link" href="#">上一页</a></li>
                <#else>
                    <li class="page-item "><a class="page-link"
                                              href="/diancan/Green/list?page=${currentPage - 1}&size=${size}">上一页</a>
                    </li>
                </#if>

                <#list 1..orderDTOPage.getTotalPages() as index>
                    <#if currentPage == index>
                        <li class="page-item disabled"><a class="page-link" href="#">${index}</a></li>
                    <#else>
                        <li class="page-item "><a class="page-link"
                                                  href="/diancan/Green/list?page=${index}&size=${size}">${index}</a>
                        </li>
                    </#if>
                </#list>

                <#if currentPage gte orderDTOPage.getTotalPages()>
                    <li class="page-item disabled"><a class="page-link" href="#">下一页</a></li>
                <#else>
                    <li class="page-item "><a class="page-link"
                                              href="/diancan/Green/list?page=${currentPage + 1}&size=${size}">下一页</a>
                    </li>
                </#if>
            </ul>
        </div>
    </div>
</div>

<#--弹窗-->
<div class="modal fade" id="myModal" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h4 class="modal-title" id="myModalLabel">
                    提醒
                </h4>
            </div>
            <div class="modal-body" id="modealContent">
                你有新的订单
            </div>
            <div class="modal-footer">
                <button onclick="stopAudio()" type="button"
                        class="btn  btn-info" data-dismiss="modal">关闭
                </button>
                <button onclick="location.reload()" type="button" class="btn btn-primary">查看订单</button>
            </div>
        </div>
    </div>
</div>

<#--播放下单音乐-->
<audio id="notice" loop="loop">
    <source src="/diancan/mp3/xiadan.m4a" type="audio/mpeg"/>
</audio>
<#--播放催单音乐-->
<audio id="notice2" loop="loop">
    <source src="/diancan/mp3/cui.m4a" type="audio/mpeg"/>
</audio>
<script type="text/javascript">
    //停止播放音乐
    function stopAudio() {
        document.getElementById('notice').pause()
        document.getElementById('notice2').pause()
    }


    var websocket = null;
    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://localhost:8080/diancan/websocket");
    } else {
        alert('Not support websocket')
    }
    //连接发生错误的回调方法
    websocket.onerror = function () {
        alert('websocket通信发生错误！');
    };

    //连接成功建立的回调方法
    websocket.onopen = function (event) {
        console.log('建立连接');
    }

    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        let msg = event.data
        console.log('收到消息:' + msg)
        //弹窗提醒, 播放音乐
        $('#myModal').modal('show');

        if (msg == 0) {
            document.getElementById('notice').play();//下单音频
        } else {
            $('#modealContent').html(msg + "的顾客催单了，请尽快出餐")
            document.getElementById('notice2').play();//催单音频
        }
    }

    //连接关闭的回调方法
    websocket.onclose = function () {
        console.log("websocket链接关闭")
    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        websocket.close();
    }
</script>
</body>
</html>