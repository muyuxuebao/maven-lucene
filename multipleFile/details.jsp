<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD Xjsp 1.0 Transitional//EN" "http://www.w3.org/TR/xjsp1/DTD/xjsp1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/jsp; charset=utf-8" />
<title>Book Store</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/book_store/style.css" />
	<link rel="stylesheet" href="lightbox.css" type="text/css" media="screen" />
	
	<script src="js/prototype.js" type="text/javascript"></script>
	<script src="js/scriptaculous.js?load=effects" type="text/javascript"></script>
	<script src="js/lightbox.js" type="text/javascript"></script>
    <script type="text/javascript" src="js/java.js"></script>
</head>
<body>
<div id="wrap">
       <div class="header">
       		<div class="logo"><a href="index.jsp"><img src="${pageContext.request.contextPath}/book_store/images/logo.gif" alt="" title="" border="0" /></a></div>            
         <div id="menu">
            <ul>                                                                       
            <li class="selected"><a href="${pageContext.request.contextPath}/book_store/index.jsp">��ҳ </a></li>
            <li><a href="${pageContext.request.contextPath}/book_store/about.jsp">��������</a></li>
              <li><a href="${pageContext.request.contextPath}/FildAllProductServlet.action">���</a></li>
            <li><a href="${pageContext.request.contextPath}/book_store/specials.jsp">�ر��鼮</a></li>
            <li><a href="${pageContext.request.contextPath}/book_store/myaccount.jsp">�ҵ��˻�</a></li>
            <li><a href="${pageContext.request.contextPath}/book_store/register.jsp">ע��</a></li>
             <li><a href="${pageContext.request.contextPath}/book_store/items.jsp">����</a></li>
            <li><a href="${pageContext.request.contextPath}/book_store/contact.jsp">��ϵ</a></li>
            </ul>
        </div>       
            
            
</div> 
       
       
        <div class="center_content">
       	<div class="left_content">
        	<div class="crumb_nav">
            <a href="index.jsp">��ҳ</a> &gt;&gt; ��Ʒչʾ
</div>
            <div class="title"><span class="title_icon"><img src="${pageContext.request.contextPath}/book_store/images/bullet1.gif" alt="" title="" /></span>��Ʒ����:&nbsp;��${product.name}��</div>
        
        	<div class="feat_prod_box_details">
            
            	<div class="prod_img"><img src="${pageContext.request.contextPath}/book_store/${product.images}" alt="" title="" border="0" />
                <br /><br />
                <a href="images/big_pic.jpg" rel="lightbox"><img src="${pageContext.request.contextPath}/book_store/images/zoom.gif" alt="" title="" border="0" /></a>
                </div>
                
                <div class="prod_det_box">
                	<div class="box_top"></div>
                    <div class="box_center">
                    <div class="prod_title">����</div>
                    <p class="details">${product.descriptions}<br />
                                       </p>
                    <div class="price"><strong>�۸�:</strong> <span class="red">${product.basePrice} $</span></div>
                    <div class="price"><strong>��ɫ:</strong> 
                    <span class="colors"><img src="${pageContext.request.contextPath}/book_store/images/color1.gif" alt="" title="" border="0" /></span>
                    <span class="colors"><img src="${pageContext.request.contextPath}/book_store/images/color2.gif" alt="" title="" border="0" /></span>
                    <span class="colors"><img src="${pageContext.request.contextPath}/book_store/images/color3.gif" alt="" title="" border="0" /></span>  
                    </div>
                    
                   <form method="post" action="${pageContext.request.contextPath}/addToCartServlet.action?productId=${product.productId}">
                    <table>
                    	<tr>
                        	<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                            <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                        	<td>
                            	����:
                            </td>
                        	<td>
                            	<select name="number">
                            		����<c:forEach begin="1" end="10" varStatus="status" step="1">
                            				<option value ='${status.count }'>${status.count }</option>
���� 									</c:forEach>
                                </select>
                            </td>
                            <td><input type="image"  src="${pageContext.request.contextPath}/book_store/images/order now.gif"></td>
                        	
                         </tr>
                    </table>
                   </form>
                    <div class="clear"></div>
                    </div>
                    
                    <div class="box_bottom"></div>
                </div>    
            <div class="clear"></div>
            </div>	
            
              
            <div id="demo" class="demolayout">

                <ul id="demo-nav" class="demolayout">
                <li><a class="active" href="#tab1">����ϸ��</a></li>
                <li><a class="" href="#tab2">������鼮</a></li>
                </ul>
    
            <div class="tabs-container">
            
                    <div style="display: block;" class="tab" id="tab1">
                                        <p class="more_details">��Ҫ�˽��������飬�����ǿ���ͨ�����µķ�ʽ�鵽ͼ�����Ϣ�����߽���Ϣ���������ǣ�</p>
                            <ul class="list">
                            <li><a href="#">���߲�ѯ��www.broadview.com.cn/06646</a></li>
                            <li><a href="#">�����ʼ���jsj@phei.com.cn Or editor@broadview.com.cn</a></li>
                            <li><a href="#">���ǵ���ϵ�绰��010-88254369</a></li>                                          
                            </ul>
                                         <p class="more_details"> �ӱ����õĸ�����Լ�����������صĶ��������У����ѿ�������һ������֮�������������ӵ�ж����ѧ���飬��C��<a target="_blank" href="http://baike.baidu.com/view/824.htm">C++</a>�Լ�Java���Զ��ж���������ļ��⣬��ͨ���׶���С��ֱ�ӵ�ʾ��������һ������ɬ����ĸ�� </p>                           
                    </div>	
                    
                            <div style="display: none;" class="tab" id="tab2">
                    <div class="new_prod_box">
                        <a href="details.jsp">product name</a>
                        <div class="new_prod_bg">
                        <a href="details.jsp"><img src="${pageContext.request.contextPath}/book_store/images/thumb1.gif" alt="" title="" class="thumb" border="0" /></a>
                        </div>           
                    </div>
                    
                    <div class="new_prod_box">
                        <a href="details.jsp">product name</a>
                        <div class="new_prod_bg">
                        <a href="details.jsp"><img src="${pageContext.request.contextPath}/book_store/images/thumb2.gif" alt="" title="" class="thumb" border="0" /></a>
                        </div>           
                    </div>                    
                    
                    <div class="new_prod_box">
                        <a href="details.jsp">product name</a>
                        <div class="new_prod_bg">
                        <a href="details.jsp"><img src="${pageContext.request.contextPath}/book_store/images/thumb3.gif" alt="" title="" class="thumb" border="0" /></a>
                        </div>           
                    </div>    

                    <div class="new_prod_box">
                        <a href="details.jsp">product name</a>
                        <div class="new_prod_bg">
                        <a href="details.jsp"><img src="${pageContext.request.contextPath}/book_store/images/thumb1.gif" alt="" title="" class="thumb" border="0" /></a>
                        </div>           
                    </div>
                    
                    <div class="new_prod_box">
                        <a href="details.jsp">product name</a>
                        <div class="new_prod_bg">
                        <a href="details.jsp"><img src="${pageContext.request.contextPath}/book_store/images/thumb2.gif" alt="" title="" class="thumb" border="0" /></a>
                        </div>           
                    </div>                    
                    
                    <div class="new_prod_box">
                        <a href="details.jsp">product name</a>
                        <div class="new_prod_bg">
                        <a href="details.jsp"><img src="${pageContext.request.contextPath}/book_store/images/thumb3.gif" alt="" title="" class="thumb" border="0" /></a>
                        </div>           
                    </div>  


                   
                    <div class="clear"></div>
                            </div>	
            
            </div>


			</div>
            

            
        <div class="clear"></div>
        </div><!--end of left content-->
        
     <div class="right_content">
        	<div class="languages_box">
            <span class="red">����:</span>
            <a href="#" class="selected"><img src="${pageContext.request.contextPath}/book_store/images/gb.gif" alt="" title="" border="0" /></a>
            <a href="#"><img src="${pageContext.request.contextPath}/book_store/images/fr.gif" alt="" title="" border="0" /></a>
            <a href="#"><img src="${pageContext.request.contextPath}/book_store/images/de.gif" alt="" title="" border="0" /></a>
            </div>
                <div class="currency">
                <span class="red">����: </span>
                <a href="#">GBP</a>
                <a href="#">EUR</a>
                <a href="#" class="selected">USD</a>
                </div>
                
                
                   <div class="cart">
              <c:if test="${(empty sessionScope.user)}">
	              <form action="${pageContext.request.contextPath}/LoginServlet.action" method="post">
	                 <table>
	                 	<tr>
	                    	<td>�˺�:</td>   <td><input type="text" size="10" name="loginname"></td>
	                        <td>����:</td>    <td><input type="password" size="12" name="loginpassword"></td>
	                        <td><input type="submit" value="�ύ"></td>
	                    </tr>
	                 </table>
	              </form>
              </c:if>
              <c:if test="${!(empty sessionScope.user)}">
              	<table>
               		<tr>
                    	<td>��ӭ${sessionScope.user.name}!!!</td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td><a href="${pageContext.request.contextPath}/LogoutServlet.action">ע��</a></td>
                    </tr>
                </table>
                <table>
                    <tr>
                    	<td>
                        	<span class="title_icon"><img src="${pageContext.request.contextPath}/book_store/images/cart.gif" alt="" title="" /></span>���ﳵ
                        </td>
                        <td>
                  				${sessionScope.cart.items} x items | <span class="red">TOTAL: ${sessionScope.cart.money}$</span>
                        </td>
                        <td>
                        	<a href="${pageContext.request.contextPath}/book_store/cart.jsp" class="view_cart">���빺�ﳵ</a>
                        </td>
                    </tr>
               </table>
             	
              </c:if>
              
              </div>
        
              <div class="title"><span class="title_icon"><img src="${pageContext.request.contextPath}/book_store/images/bullet3.gif" alt="" title="" /></span>��ǽ��� Story</div> 
             <div class="about">
             <p>
             <img src="${pageContext.request.contextPath}/book_store/images/about.gif" alt="" title="" class="right" />
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;���������ȫ�������ۺ����������Ϲ����̳ǣ��ɹ�����������������Ĺ�˾������  ������
�ϻ���������IDG���š�¬ɭ�����ż��š����޴�ҵͶ�ʻ���ԭ�������й���ҵ���𣩹�ͬͶ�ʳ�����
             </p>
             
    </div>
             
             <div class="right_box">
             
             	<div class="title"><span class="title_icon"><img src="${pageContext.request.contextPath}/book_store/images/bullet4.gif" alt="" title="" /></span>����</div> 
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
             
             	<div class="title"><span class="title_icon"><img src="${pageContext.request.contextPath}/book_store/images/bullet5.gif" alt="" title="" /></span>���</div> 
                
                <ul class="list">
                <li><a href="#">��վ����</a></li>
                <li><a href="#">���ݿ���</a></li> 
                <li><a href="#">ͼ��ͼ��</a></li>
                <li><a href="#">�������</a></li>
                <li><a href="#">�ִ��칫</a></li>
                <li><a href="#">����ϵͳ</a></li>
                <li><a href="#">������֤</a></li>
                <li><a href="#">���缼��</a></li>
                <li><a href="#">�������</a></li>
                 <li><a href="#">���Կ���</a></li>
                <li><a href="#">��ѧ��Ʒ</a></li>                                              
                </ul>
                
   	         <div class="title"><span class="title_icon"><img src="${pageContext.request.contextPath}/book_store/images/bullet6.gif" alt="" title="" /></span>�ϻ���</div> 
                
                <ul class="list">
                <li><a href="#">����΢��</a></li>
                <li><a href="#">��ѶQQ</a></li>
                <li><a href="#">��������</a></li>
                <li><a href="#">��ѩ��˾</a></li>
                <li><a href="#">������</a></li>
                <li><a href="#">�����̳�</a></li>
                <li><a href="#">�Ĳ����Թ�˾</a></li>
                <li><a href="#">����԰��</a></li>
                <li><a href="#">���۹�湫˾</a></li>                              
                </ul>      
             
             </div>     
             
        
        </div><!--end of right content-->
        
        
       
       
       <div class="clear"></div>
       </div><!--end of center content-->
       
              
       <div class="footer">
       	<div class="left_footer"><img src="${pageContext.request.contextPath}/book_store/images/footer_logo.gif" alt="" title="" /><br /> <a href="http://csscreme.com"><img src="${pageContext.request.contextPath}/book_store/images/csscreme.gif" alt="by csscreme.com" title="by csscreme.com" border="0" /></a></div>
         <div class="right_footer">
        <a href="index.jsp">��ҳ</a><a href="#"></a>
        <a href="about.jsp">��������</a><a href="#"></a>
        <a href="#">����</a>
        <a href="#">˽��Ȩ��</a>
        <a href="#">��ϵ����</a>
       
        </div>
        
       
       </div>
    

</div>

</body>
<script type="text/javascript">

var tabber1 = new Yetii({
id: 'demo'
});

</script>
</html>