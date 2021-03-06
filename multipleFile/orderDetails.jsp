<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD Xjsp 1.0 Transitional//EN" "http://www.w3.org/TR/xjsp1/DTD/xjsp1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/jsp; charset=utf-8" />
<title>Book Store</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/book_store/style.css" />
</head>
<body>
<div id="wrap">

       <div class="header">
       		<div class="logo"><a href="index.jsp"><img src="${pageContext.request.contextPath}/book_store/images/logo.gif" alt="" title="" border="0" /></a></div>            
         <div id="menu">
            <ul>                                                                       
            <li class="selected"><a href="${pageContext.request.contextPath}/book_store/index.jsp">首页 </a></li>
            <li><a href="${pageContext.request.contextPath}/book_store/about.jsp">关于我们</a></li>
              <li><a href="${pageContext.request.contextPath}/FildAllProductServlet.action">书架</a></li>
            <li><a href="${pageContext.request.contextPath}/book_store/specials.jsp">特别书籍</a></li>
            <li><a href="${pageContext.request.contextPath}/book_store/myaccount.jsp">我的账户</a></li>
            <li><a href="${pageContext.request.contextPath}/book_store/register.jsp">注册</a></li>
            <li><a href="${pageContext.request.contextPath}/book_store/items.jsp">条款</a></li>
            <li><a href="${pageContext.request.contextPath}/book_store/contact.jsp">联系</a></li>
            </ul>
        </div>     
         <blockquote>&nbsp;</blockquote>
  </div> 
       
       
       <div class="center_content">
       	<div class="left_content">
            <div class="title"><span class="title_icon"><img src="${pageContext.request.contextPath}/book_store/images/bullet1.gif" alt="" title="" /></span>订单详情</div>
        
        	<div class="feat_prod_box_details">
        	
        	
        	
            
            <table>
            	<tr>
            		<td>
            			<strong><font color="#990000">订单号:</font></strong>
            		</td>
            		<td>
            			${requestScope.order.orderId }
            		</td>
            		<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
            		<td>
            			<strong><font color="#990000">用户名:</font></strong>
            		</td>
            		<td>
            			${sessionScope.user.name }
            		</td>
            		<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
            		<td>
            			<strong><font color="#990000">订单生成日期:</font></strong>
            		</td>
            		<td>${requestScope.order.name}</td>
            		
            	</tr>
            	<tr><td><strong><font color="#990000">购买商品:</font></strong></td></tr>
            </table>
            <table class="cart_table">
            	<tr class="cart_title">
                	<td>图片</td>
                	<td>书名</td>
                    <td>单价</td>
                    <td>数量</td>
                    <td>花费</td>  
                </tr>
                
                
                
           
                 <c:forEach var="orderline" items="${requestScope.orderlines}" >
		                 <tr>
		                	<td><a href="${pageContext.request.contextPath}/FindProductDetails.action?productId=${orderline.product.productId}"><img src="${pageContext.request.contextPath}/book_store/${orderline.product.images}" alt="" title="" border="0" width="40" /></a></td>
		                	<td>${orderline.product.name }</td>
		                    <td>${orderline.product.basePrice }$</td>
		                    <td>${orderline.amount }</td>
		                    <td>${orderline.product.basePrice*orderline.amount }$</td> 
		                    <td></td>
		                </tr>          
 	  		  	</c:forEach>
                
                
                
            	
                <tr>
                <td colspan="4" class="cart_total"><span class="red">总运费:</span></td>
                <td> 0$</td>                
                </tr>  
                
                <tr>
                <td colspan="4" class="cart_total"><span class="red">合计:</span></td>
                <td> ${requestScope.order.cost }</td>                
                </tr>                  
            
            </table>
          
           
            
            
        
        
            
            

             
            
          </div>	
            
              

            

            
        <div class="clear"></div>
        </div><!--end of left content-->
        
        <div class="right_content">
        	<div class="languages_box">
            <span class="red">语言:</span>
            <a href="#" class="selected"><img src="${pageContext.request.contextPath}/book_store/images/gb.gif" alt="" title="" border="0" /></a>
            <a href="#"><img src="${pageContext.request.contextPath}/book_store/images/fr.gif" alt="" title="" border="0" /></a>
            <a href="#"><img src="${pageContext.request.contextPath}/book_store/images/de.gif" alt="" title="" border="0" /></a>
            </div>
                <div class="currency">
                <span class="red">货币: </span>
                <a href="#">GBP</a>
                <a href="#">EUR</a>
                <a href="#" class="selected">USD</a>
                </div>
                
                
                       <div class="cart">
              <c:if test="${(empty sessionScope.user)}">
	              <form action="${pageContext.request.contextPath}/LoginServlet.action" method="post">
	                 <table>
	                 	<tr>
	                    	<td>账号:</td>   <td><input type="text" size="10" name="loginname"></td>
	                        <td>密码:</td>    <td><input type="password" size="12" name="loginpassword"></td>
	                        <td><input type="submit" value="提交"></td>
	                    </tr>
	                 </table>
	              </form>
              </c:if>
              <c:if test="${!(empty sessionScope.user)}">
              	<table>
               		<tr>
                    	<td>欢迎${sessionScope.user.name}!!!</td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td><a href="${pageContext.request.contextPath}/LogoutServlet.action">注销</a></td>
                    </tr>
                </table>
                <table>
                    <tr>
                    	<td>
                        	<span class="title_icon"><img src="${pageContext.request.contextPath}/book_store/images/cart.gif" alt="" title="" /></span>购物车
                        </td>
                        <td>
                  				${sessionScope.cart.items} x items | <span class="red">TOTAL: ${sessionScope.cart.money}$</span>
                        </td>
                        <td>
                        	<a href="${pageContext.request.contextPath}/book_store/cart.jsp" class="view_cart">进入购物车</a>
                        </td>
                    </tr>
               </table>
             	
              </c:if>
              
              </div>
        
            <div class="title"><span class="title_icon"><img src="${pageContext.request.contextPath}/book_store/images/bullet3.gif" alt="" title="" /></span>书城介绍 Story</div> 
             <div class="about">
             <p>
             <img src="${pageContext.request.contextPath}/book_store/images/about.gif" alt="" title="" class="right" />
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;我们书店是全较最大的综合性中文网上购物商城，由国内著名出版机构科文公司、美国  当当网
老虎基金、美国IDG集团、卢森堡剑桥集团、亚洲创业投资基金（原名软银中国创业基金）共同投资成立。
             </p>
             
    </div>
             
               <div class="right_box">
             
             	<div class="title"><span class="title_icon"><img src="${pageContext.request.contextPath}/book_store/images/bullet4.gif" alt="" title="" /></span>促销</div> 
                    <div class="new_prod_box">
                        <a href="${pageContext.request.contextPath}/FindProductDetails.action?productId=${sessionScope.all_product1.productId}">${all_product1.name}</a>
                        <div class="new_prod_bg">
                        <span class="new_icon"><img src="${pageContext.request.contextPath}/book_store/images/promo_icon.gif" alt="" title="" /></span>
                        <a href="${pageContext.request.contextPath}/FindProductDetails.action?productId=${sessionScope.all_product1.productId}"><img src="${pageContext.request.contextPath}/book_store/${sessionScope.all_product1.images}" alt="" title="" class="thumb" border="0" /></a>
                        </div>           
                    </div>
                    
                    <div class="new_prod_box">
                        <a href="${pageContext.request.contextPath}/FindProductDetails.action?productId=${sessionScope.all_product2.productId}">${all_product2.name}</a>
                        <div class="new_prod_bg">
                        <span class="new_icon"><img src="${pageContext.request.contextPath}/book_store/images/promo_icon.gif" alt="" title="" /></span>
                        <a href="${pageContext.request.contextPath}/FindProductDetails.action?productId=${sessionScope.all_product2.productId}"><img src="${pageContext.request.contextPath}/book_store/${sessionScope.all_product2.images}" alt="" title="" class="thumb" border="0" /></a>
                        </div>           
                    </div>                   
                    
                    <div class="new_prod_box">
                        <a href="${pageContext.request.contextPath}/FindProductDetails.action?productId=${sessionScope.all_product3.productId}">${all_product3.name}</a>
                        <div class="new_prod_bg">
                        <span class="new_icon"><img src="${pageContext.request.contextPath}/book_store/images/promo_icon.gif" alt="" title="" /></span>
                        <a href="${pageContext.request.contextPath}/FindProductDetails.action?productId=${sessionScope.all_product3.productId}"><img src="${pageContext.request.contextPath}/book_store/${sessionScope.all_product3.images}" alt="" title="" class="thumb" border="0" /></a>
                        </div>           
                    </div>             
             
             </div>
             
         <div class="right_box">
             
             	<div class="title"><span class="title_icon"><img src="${pageContext.request.contextPath}/book_store/images/bullet5.gif" alt="" title="" /></span>类别</div> 
                
                <ul class="list">
                <li><a href="#">网站建设</a></li>
                <li><a href="#">数据库类</a></li> 
                <li><a href="#">图形图像</a></li>
                <li><a href="#">程序设计</a></li>
                <li><a href="#">现代办公</a></li>
                <li><a href="#">操作系统</a></li>
                <li><a href="#">考试认证</a></li>
                <li><a href="#">网络技术</a></li>
                <li><a href="#">软件工程</a></li>
                 <li><a href="#">电脑刊物</a></li>
                <li><a href="#">文学作品</a></li>                                              
                </ul>
                
   	         <div class="title"><span class="title_icon"><img src="${pageContext.request.contextPath}/book_store/images/bullet6.gif" alt="" title="" /></span>合伙人</div> 
                
                <ul class="list">
                <li><a href="#">新浪微博</a></li>
                <li><a href="#">腾讯QQ</a></li>
                <li><a href="#">网邮箱易</a></li>
                <li><a href="#">暴雪公司</a></li>
                <li><a href="#">当当网</a></li>
                <li><a href="#">京都商城</a></li>
                <li><a href="#">文博电脑公司</a></li>
                <li><a href="#">安博园区</a></li>
                <li><a href="#">云舟广告公司</a></li>                              
                </ul>      
             
             </div>    
             
        
        </div><!--end of right content-->
        
        
       
       
       <div class="clear"></div>
       </div><!--end of center content-->
       
              
       <div class="footer">
       	<div class="left_footer"><img src="${pageContext.request.contextPath}/book_store/images/footer_logo.gif" alt="" title="" /><br /> <a href="http://csscreme.com"><img src="${pageContext.request.contextPath}/book_store/images/csscreme.gif" alt="by csscreme.com" title="by csscreme.com" border="0" /></a></div>
        <div class="right_footer">
        <a href="index.jsp">首页</a><a href="#"></a>
        <a href="about.jsp">关于我们</a><a href="#"></a>
        <a href="#">服务</a>
        <a href="#">私人权限</a>
        <a href="#">联系我们</a>
       
        </div>
        
       
       </div>
    

</div>

</body>
</html>