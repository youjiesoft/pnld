/*
*do:扫码页面公共JS
*Author:YouJieGroup
*date:2018/6/15 16:53
*/


// 禁止选择仓库
function notSck(){
    $("#canzhao").attr("disabled",true);
    $("#isbaocun").attr("disabled",false);
    $("#istempbaocun").attr("disabled",false);
    $("#canzhao").removeClass();
    $("#canzhao").addClass("btn btn-default btn-sm");
}

// 没有表体数据不能保存
function issavetemp(){
    $("#istempbaocun").attr("disabled",true);
}
//条码回填
//@message 条码
function show(message)
{
   document.getElementById('tiaoma').value = message;
}
//条码请求数据回调处理
function databack(jsondata){
    // 随机数
    var rannum = Math.floor(Math.random() * 900 + 100);
    var timestamp = (new Date()).valueOf();
    //jsondata[0]["gformid"] = getRanNum()+timestamp+rannum;
    var jsonHval = JSON.stringify(jsondata);
    //alert(jsonHval);
    var inputs = $(".box div input");
    inputs.each(function(i,val){
        for(var item in jsondata[0]){
            if($(val).attr("datafield") && $(val).attr("datafield") == item){
                $(val).val(jsondata[0][item]);
            }
        }
    });

    // 选中radio
    for(var i in jsondata){
        if(jsondata[0]["bredvouch"] == "0"){
            $("#lan").prop("checked", "checked");
        }else{
            $("#hong").prop("checked", "checked");
        }
    }

}

// 标体
function databackB(jsondata){
    //var jsonHval = JSON.stringify(jsondata);
    //alert(jsonHval);
    var inputs = $(".box div input");
    inputs.each(function(i,val){
        for(var item in jsondata[0]){
            if($(val).attr("datafield") && $(val).attr("datafield") == item){
                $(val).val(jsondata[0][item]);
            }
        }
    });
}
//返回后重新渲染表头页面
function databacktwo(jsonHeadertwo){
    var jsonHval = JSON.stringify(jsonHeadertwo);
    // 如果在扫描表体页面返回时，查看是否有数据
    // 如果有数据，则扫码按钮，保存按钮  为启用状态
    if(jsonHval == null){
        // 按钮禁用
         $("#isjihuo").attr("disabled",true);
         //$("#isbaocun").attr("disabled",true);
    }else{
        //按钮解禁
          $("#isjihuo").attr("disabled",false);
          //$("#isbaocun").attr("disabled",false);
    }
    var inputs = $(".box div input");
    inputs.each(function(i,val){
        for(var item in jsonHeadertwo){
            if($(val).attr("datafield") && $(val).attr("datafield")==item){
                $(val).val(jsonHeadertwo[item]).change();
            }
        }
    });

     // 选中radio
        for(var i in jsonHeadertwo){
            if(jsonHeadertwo["bredvouch"] == "0"){
                $("#lan").prop("checked", "checked");
            }else{
                $("#hong").prop("checked", "checked");
            }
        }
}

//条码请求数据回调处理
function databackthree(jsondata){
    var jsonHval = JSON.stringify(jsondata);
    //alert(jsonHval);
    var inputs = $(".box div input");
    inputs.each(function(i,val){
        for(var item in jsondata){
            if($(val).attr("datafield") && $(val).attr("datafield")==item){
                $(val).val(jsondata[item]);
            }
        }
    });
}
//js调用android页面返回方法
function back(){
    window.jsbridge.back();
}
//验证页面上是否存在不为空的值
function backout(){
    var inputs = document.getElementsByName("outputform");
    var str = "0";
    if($("#cdlcode").val() != ""){
                var str = "1";
    }
    window.jsbridge.back(str);
}
// 生成随机4位字母
function getRanNum(){
   var result = [];
       for(var i=0;i<4;i++){
          var ranNum = Math.ceil(Math.random() * 25); //生成一个0到25的数字
           //大写字母'A'的ASCII是65,A~Z的ASCII码就是65 + 0~25;然后调用String.fromCharCode()传入ASCII值返回相应的字符并push进数组里
           result.push(String.fromCharCode(65+ranNum));
       }
   return  result.join('');
}

//扫码页面跳转
function jumppage(){
    // 判断是否选择仓库
    if(!$("input[datafield='cwhname']").val()){
        alert("请选择仓库！");
    }
     /*处理自己选择仓库，重新获取表头数据，提交到全局变量中覆盖已经存储的表体*/
    // 获取蓝字红字选中值
    var rchecked = $("input[name='radio']:checked").val();
    // 随机数
    var rannum = Math.floor(Math.random() * 900 + 100);
    var timestamp = (new Date()).valueOf();
    var statues = 0;
    var dataHval = {};
    //每个单据的唯一标识
    dataHval["gformid"] = $("input[datafield='gformid']").val();
    //$("input[datafield='gformid']").val(getRanNum(4)+timestamp+rannum);
    //是否已经入库标识
    dataHval["statues"] = statues;
    dataHval["bredvouch"] = rchecked;
    var inputs = $(".box .boxbodyone div input");
    var names = $("input[name='outputform']");
    // 扫码后按钮解禁
    if($("#tiaoma").val() != ""){
        $("#isjihuo").attr("disabled",false);
        //$("#isbaocun").attr("disabled",false);
    }
    inputs.each(function(i,val){
       dataHval[$(val).attr("datafield")] = names[i].value;
    });
    var jsonHval = JSON.stringify(dataHval);
    //alert(jsonHval);
    window.jsbridge.jumppage(jsonHval);
    /*处理自己选择仓库，重新获取表头数据，提交到全局变量中覆盖已经存储的表体*/
    // 老跳转
    //window.jsbridge.jumppage();
}
// 跳转已扫明细
function jumppagelist()
{
     window.jsbridge.jumppagelist();
}
//新增
function add(){
    window.jsbridge.add();
}
//获取表头值返回给android方法
function formHdata(){
    // 获取蓝字红字选中值
    var rchecked = $("input[name='radio']:checked").val();
    // 随机数
    var rannum = Math.floor(Math.random() * 900 + 100);
    var timestamp = (new Date()).valueOf();
    var statues = 0;
    var dataHval = {};

    //每个单据的唯一标识时间戳加随机数
    dataHval["gformid"] = $("input[datafield='gformid']").val();
    //是否已经入库标识
    dataHval["statues"] = statues;
    dataHval["bredvouch"] = rchecked;

    var inputs = $(".box .boxbodyone div input");
    var names = $("input[name='outputform']");

    // 参照的仓库 仓库码
//    var cwhname = $("#cwhname").val();
//    var cwhcode = $("#cwhcode").val();
    // 扫码后按钮解禁
    if($("#tiaoma").val() != ""){
         $("#isjihuo").attr("disabled",false);
         //$("#isbaocun").attr("disabled",false);
    }
    inputs.each(function(i,val){
       dataHval[$(val).attr("datafield")] = names[i].value;
    });
    var jsonHval = JSON.stringify(dataHval);
    window.jsbridge.dataHform(jsonHval);

    //alert(jsonHval);
    //
   /* if(cwhname.length > 0 || cwhcode.length >0){
        var statusMsg = "表头已存储!"
        window.jsbridge.dataHform(jsonHval);
    }else{
        $("#isjihuo").attr("disabled",true);
        $("#isbaocun").attr("disabled",true);
        var statusMsg = "表头未存储,请选择仓库后重新扫描!"
    }

    // 提示消息
    window.jsbridge.alertMsg(statusMsg);*/
}

//获取表体值返回给android方法
function formBdata(){
    // 获取条形码
    var barcode = $("input[datafield='idlsid']").val();
    var dataBval = {};
    dataBval["barcode"] = barcode;
    var inputs = $(".box div input");
    var names = $("input[name='outputform']");
    inputs.each(function(i,val){
        dataBval[$(val).attr("datafield")] = names[i].value;
     });

    var jsonBval = JSON.stringify(dataBval);
    //alert(jsonBval);
    window.jsbridge.dataBform(jsonBval);
}
//点击保存生成单据
function savedata(){
     // 点击保存时，先禁用按钮、
     $("#isbaocun").attr("disabled",true);
     //单据唯一标识
     var gformid = $("input[datafield='gformid']").val();
     window.jsbridge.savedata(gformid);
}

//扫描明细List回填
function databacklist(arrayList){
    var jsonHval = JSON.stringify(arrayList);
    // 明细条数
    $("#listbody tr").remove();
    for(var i=0;i<arrayList.length;i++){
        var cinvname = arrayList[i]['cinvname'];//存货名称
        var iquantity = arrayList[i]['iquantity'];//数量
        var cbatch = arrayList[i]['cbatch'];//批号
        var idlsid = arrayList[i]['idlsid'];//条形码
        var jsoneditdata = JSON.stringify(arrayList[i]);
        // 表格上面删除
        /*var html = "<tr><td>"+(i+1)+"</td><td>"+cinvname+"</td><td>"+iquantity+"</td><td>"+cbatch+"</td><td><input class='delbtn' data-index='"+i+"' type='button' name='button' id='button' value='删除' /></td></tr>";
                                              $("#listbody").append(html);*/
        var html = "<tr onclick='detailist("+jsoneditdata+","+i+")'><td style='height:45px'>"+(i+1)+"</td><td>"+cinvname+"</td><td>"+iquantity+"</td><td>"+cbatch+"</td></tr>";
        $("#listbody").append(html);
       // 删除表格行  在表格上面删除
       /*$(".delbtn").on('click',function(){

           //<<1 数据库删除记录
           window.jsbridge.del(idlsid);
           //<<2 页面上删除
           $(this).parents("tr").remove();

           //<<3 全局变量删除

       });*/

    }

    //修改查看出库单  除开最后一列
     //var tableObj = $("table tr td:not(:last-child)");
    /* var tableObj = $("table tbody tr");
     tableObj.each(function(i){
         $(this).on('click',function(){
             window.jsbridge.edit(jsoneditdata,i);
         })
     });*/
}

// 扫描明细的跳转，带数据
function detailist(jsoneditdata,i){
    var jsoneditdata = JSON.stringify(jsoneditdata);
    window.jsbridge.edit(jsoneditdata,i);
}


// 修改传递数据
function senddata(jsoneditdata){
    var jsoneditdata = JSON.stringify(jsoneditdata);
    window.jsbridge.edit(jsoneditdata);
}

// 暂存明细数量

//已扫数量
function backlistnum(listnum){
    listnum = Number(listnum)+1;
    $("#soamiaonum").html(listnum);
}

//返回扫码时 已扫数量  处理index值+1问题
function backlistnum_(listnum){
    listnum = Number(listnum);
    $("#soamiaonum").html(listnum);
}

//页面当前明细条数
function totlenum(listnum){
    listnum = Number(listnum);
    $("#totlenum").html(listnum);
}

//页面当前明细条数，处理Index-1的问题
function totlenum_(listnum){
    listnum = Number(listnum)-1;
    $("#totlenum").html(listnum);
}

//开启仓库参照遮罩层
function canzhao(){
     //显示遮罩层
     $('#overlay').show();
}

//关闭仓库参照遮罩层
 function closezz(){
      $('#overlay').hide();
}

// 仓库信息数据回填
function ckdataback(jsondatack){
 //var val = JSON.stringify(jsondatack);alert(val);
    //遮罩层
    var html = '<div id="overlay">'+
        '<button onclick="closezz();" style="margin-left:73%;width:20%;margin-top:2%;background-color:#00EEEE">关闭</button>'+
        '<div style="width:95%;height:85%;margin-left:1%;margin-top:3%">'+
            '<table cellspacing="1" cellpadding="1" border="1" style="width:100%;">'+
                '<thead><tr>'+
                    '<th>仓库序号</th><th>仓库名</th><th>操作</th>'+
                    '</tr></thead><tbody id="precontent">'+
                '</tbody></table></div>'+
         '</div>';
    $('body').append(html);
    var docHeight = $(document).height(); //获取窗口高度
    $('#overlay').height(docHeight).css({
         'opacity': .9, //透明度
         'position': 'absolute',
         'top': 0,
         'left': 0,
         'background-color': '#FFFFFF',
         'width': '100%',
         'height':'100%',
         'z-index': 5000 //保证这个悬浮层位于其它内容之上
    });
    //表格样式
    $('#overlay table,th,td').css({'border':'1px solid blue',});
    //先隐藏
    $('#overlay').hide();
    for(var i in jsondatack){
        var cwhcode = jsondatack[i]["cwhcode"];
        var cwhname = jsondatack[i]["cwhname"];
        $('#precontent').append('<tr onclick="lookupck(this);" style="height:55px"><td class="cwhcode">'+cwhcode+'</td><td class="cwhname">'+cwhname+'</td><td>带回</td></tr>');
    }
}

// 选择仓库回填
function lookupck(obj){
     var name = $(obj).find(".cwhname").text();
     var cwhcode = $(obj).find(".cwhcode").text();
     $('#cwhname').val(name); // 回填仓库名
     $('#cwhcode').val(cwhcode); // 回填仓库码
     //回填后隐藏
     $('#overlay').hide()

     // 获取页面数据
     // 获取蓝字红字选中值
     var rchecked = $("input[name='radio']:checked").val();
     statues = 0;
     var dataHval = {};
     //每个单据的唯一标识
     dataHval["gformid"] = $("input[datafield='gformid']").val();
     //是否已经入库标识
     dataHval["statues"] = statues;
     dataHval["bredvouch"] = rchecked;
     var inputs = $(".box .boxbodyone div input");
     var names = $("input[name='outputform']");
     inputs.each(function(i,val){
         dataHval[$(val).attr("datafield")] = names[i].value;
     });
     var jsonHval = JSON.stringify(dataHval);
     //alert(jsonHval);
     // 修改sqlite中的表头数据
     window.jsbridge.editHead(jsonHval);
}


// 销售出库单列表 表格数据渲染
function backsaledata(jsondatack){
    var jsoneditdata = JSON.stringify(jsondatack);
    //alert(jsoneditdata);
    // 明细条数
    //$("#lists tr").remove();
    for(var i in jsondatack){
       var ccodes = jsondatack[i]["ccode"];
       var ccusnames = jsondatack[i]["ccusname"];
       var cmakers = jsondatack[i]["cmaker"];
       var cwhnames = jsondatack[i]["cwhname"];
       var htmls = "<tr height='65' onclick='showthis(\""+ccodes+"\")'><td style='45px'>"+ccodes+"</td><td>"+ccusnames+"</td><td width='48'>"+cmakers+"</td></tr>";
       $("#lists").append(htmls);
    }
}

// 销售出库单列表 失败列表表格数据渲染
/*function backsaledataerr(jsondatackerr){
var jsoneditdata = JSON.stringify(jsondatackerr);
    // 明细条数
    for(var i in jsondatackerr){
       var ccodes = jsondatackerr[i]["ccode"].toString();
       var ccusnames = jsondatackerr[i]["ccusname"];
       var cmakers = jsondatackerr[i]["cmaker"];
       var cwhnames = jsondatackerr[i]["cwhname"];
       var htmls = "<tr onclick='showthis(\""+ccodes+"\")'><td style='45px'>"+ccodes+"</td><td>"+ccusnames+"</td><td>"+cmakers+"</td></tr>";
       $("#list").append(htmls);
    }

    //alert($("#list").find("tr").length);
}*/

// 销售出库单条查看
function showthis(ccodes){
    window.jsbridge.jumpdetail(ccodes);
}


// 销售出库列表数量
function listnumsale(listnumsale){
    $("#listnumsale").html(listnumsale);
}

//测试消息提示
function scanTestMsg(errmessage){
    alert(1);
}


//清空表头表单的值
function databackclear(){
    $("input[name='outputform']").val("");
    $("#isjihuo").attr("disabled",true);
    //$("#isbaocun").attr("disabled",true);
}


// 暂存列表
/*function tempdata(arrayList){
//    var jsoneditdata = JSON.stringify(arrayList);
//    alert(jsoneditdata);
    for(var i in arrayList){
        var dataHval = {};
        var id = arrayList[i]['id'];
        var cdlcode = arrayList[i]["cdlcode"];
        var crdname = arrayList[i]["crdname"];
        var cpersonname = arrayList[i]["cpersonname"];
        var bredvouch = arrayList[i]["bredvouch"];
        dataHval['id'] = id;
        dataHval['cdlcode'] = cdlcode;
        dataHval['bredvouch'] = bredvouch;
        var jsondata = JSON.stringify(dataHval);//重新提交数据
        var arrjsonshow = JSON.stringify(arrayList[i]);//单条查看数据
        var htmls = "<tr onclick='savereset(this,"+jsondata+","+arrjsonshow+")'><td>"+id+"</td><td style='45px'>"+cdlcode+"</td><td>"+crdname+"</td><td>"+cpersonname+"</td></tr>";
        $("#lists").append(htmls);
    }
}*/

// 失败单据列表渲染
function backsaledataerr(arrayList){
    var jsoneditdata = JSON.stringify(arrayList);
    //alert(jsoneditdata);
    if(arrayList.length > 0){
        window.jsbridge.msg();
    }
    //alert(jsoneditdata);
    for(var i in arrayList){
        // dataHval 重新提交数据需要数据
        var dataHval = {};
        var id = arrayList[i]['id'];
        var cdlcode = arrayList[i]["cdlcode"];
        var creatime = arrayList[i]['createtime'];
        var crdname = arrayList[i]["crdname"];
        var cpersonname = arrayList[i]["cpersonname"];
        var bredvouch = arrayList[i]["bredvouch"];
        var t_gformid = arrayList[i]["gformid"];
        dataHval['id'] = id;
        dataHval['cdlcode'] = cdlcode;
        dataHval['bredvouch'] = bredvouch;
        var jsondata = JSON.stringify(dataHval);//重新提交数据
        var arrjsonshow = JSON.stringify(arrayList[i]);//单条查看数据
        var htmls = "<tr height='60' onclick='savereset(this,"+jsondata+","+arrjsonshow+")'><td width='40'>"+id+"</td><td>"+arrayList[i]['createtime']+"</td><td>"+cdlcode+"</td><td width='66'>"+cpersonname+"</td></tr>";
        $("#list").append(htmls);
    }
}


// 将单条数据重新传入页面，传入单条数据
function savereset(obj,jsondata,arrjsonshow){
    var jsondata = JSON.stringify(jsondata);
    var arrjsonshow = JSON.stringify(arrjsonshow);
    var texts = $(obj).parents("tr").text();
    window.jsbridge.jumppage(jsondata,arrjsonshow);
}

// 暂存页面渲染
function tempHfrom(tempheader){
    var jsonHval = JSON.stringify(tempheader);
    //alert(jsonHval);
    var inputs = $(".box div input");
    inputs.each(function(i,val){
        for(var item in tempheader){
            if($(val).attr("datafield") && $(val).attr("datafield")==item){
                $(val).val(tempheader[item]);
            }
        }
    });

    // 选中radio
    for(var i in tempheader){
        if(tempheader["bredvouch"] == "0"){
            $("#lan").prop("checked", "checked");
        }else{
            $("#hong").prop("checked", "checked");
        }
    }
}

// 重新保存单据
function savetemp(){
    window.jsbridge.savereset();
}

// 销售出库详情的页面数据渲染
function outshowa(jsondatacklist){
    var jsonHval = JSON.stringify(jsondatacklist);
    var inputs = $(".box div input");
    inputs.each(function(i,val){
        for(var item in jsondatacklist[0]){
            if($(val).attr("datafield") && $(val).attr("datafield")==item){
                $(val).val(jsondatacklist[0][item]);
            }
        }
    });
}

// 加载条，待开发
function loading(){
    alert("我是加载条");
}

/*暂存，重新保存页面的扫码跳转，表头值传递给表体页面*/
 function scantemp(){
        // 获取蓝字红字选中值
    var rchecked = $("input[name='radio']:checked").val();
    // 随机数
    var rannum = Math.floor(Math.random() * 900 + 100);
    var timestamp = (new Date()).valueOf();
    var statues = 0;
    var dataHval = {};

    //每个单据的唯一标识时间戳加随机数
    //$("input[datafield='gformid']").val();
    //是否已经入库标识
    dataHval["statues"] = statues;
    dataHval["bredvouch"] = rchecked;

    var inputs = $(".box .boxbodyone div input");
    var names = $("input[name='outputform']");
    inputs.each(function(i,val){
       dataHval[$(val).attr("datafield")] = names[i].value;
    });
    var jsonHval = JSON.stringify(dataHval);
    //alert(jsonHval);
    window.jsbridge.scantemp(jsonHval);
 }
