<style>
    * {
        margin: 0;
        padding: 0;
    }

    a:hover, a:focus {
        text-decoration: none
    }


    .mobile-nav ul li a {
        color: black;
        font-size: 1.6em;
        display: block;
        padding: 1em 15%;
        border-bottom: 1px solid #6E8B3D;
        transition: all 0.2s ease-out;
        cursor: pointer;
    }

    .mobile-nav {
        position: fixed;
        top: 0;
        left: 0;
        width: 180px;
        height: 100%;
        background-color: #d4edda;
        z-index: 9999;
        transition: all 0.3s ease-in;
    }

    .mobile-nav ul li a:hover {
        background-color: #138496;
        color: #ffffff;
    }

</style>

<div id="mobile-menu" class="mobile-nav ">
    <ul id="leftRoot">
        <li><a href="/diancan/home/homeList">收入收购</a></li>
        <li><a href="/diancan/adimOrder/list">订单管理</a></li>
        <li><a href="/diancan/Green/list">定制餐品</a></li>
        <li><a href="/diancan/food/list">上架菜品</a></li>
        <li><a href="/diancan/leimu/list">菜品类目</a></li>


        <li><a href="/diancan/logoutAdmin">退出登陆</a></li>

    </ul>
</div>


