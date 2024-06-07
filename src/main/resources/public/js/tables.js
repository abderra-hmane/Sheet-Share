document.addEventListener("DOMContentLoaded",function(){
    let add_table_button = document.querySelector(".add-table");
    let add_table_container =document.querySelector(".add-table-container");
    let spreadsheet_select = document.querySelector(".spreadsheet-selecting");
    let creat_table = document.querySelector(".creat-table");
    let sheet = document.querySelectorAll(".select-sheet");
    
    add_table_button.addEventListener("click",function(e){
        add_table_container.classList.remove("add-table-container-close");
    });
    add_table_container.addEventListener('click',function(e){
        if (add_table_container == (e.target)) {
            add_table_container.classList.add("add-table-container-close")
           
        }
      
        
    })
    spreadsheet_select.addEventListener("change",function(e){
        
        var selectedValue = e.target.value;
        
        
        document.querySelectorAll(".select-sheet").forEach(function(element){
            element.classList.add("d-none")
        })
        document.querySelector(".select-"+selectedValue).classList.remove("d-none");

        
    })
   

    creat_table.addEventListener("click",function(e){
        
        var fetching_link = "/creatTable?";
        var spreadsheet = spreadsheet_select.value;
        fetching_link+="spreadsheet="+spreadsheet+"&";
        var table_name = document.querySelector(".table-name").value;
        fetching_link+="table_name="+table_name+"&";
        var sheet =  document.querySelector(".select-"+spreadsheet).value.split("-")[0];
        fetching_link+="sheet="+sheet+"&";
        var start_column_range =document.querySelector(".table-range:not(.d-none) .start-column-range").value;
        fetching_link+="start_col="+start_column_range+"&";
        var start_row_range = document.querySelector(".table-range:not(.d-none) .start-row-range").value
        fetching_link+="start_row="+start_row_range+"&";
        var end_column_range = document.querySelector(".table-range:not(.d-none) .end-column-range").value
        fetching_link+="end_col="+end_column_range+"&";
        var end_row_range = document.querySelector(".table-range:not(.d-none) .end-row-range").value
        fetching_link+="end_row="+end_row_range+"&";
        for(var i=start_column_range;i<=end_column_range;i++){
       
        fetching_link+="column_"+i+"="+document.querySelector(".column-"+i).value+"&";
        }
        
        
        fetch(fetching_link).then(
            response=>{
                if(response.status == 200){
                    window.location.reload();
                }
            }
        )
        

    });

    sheet.forEach(function(element){
        element.addEventListener("change",function(e){
            document.querySelectorAll(".table-range").forEach(function(table){
                table.classList.add("d-none");
            })
            document.querySelector(".range-"+element.value).classList.remove("d-none");
            
            column_name(element.value);
            
            
        })
    })
    document.querySelectorAll(".table-range:not(.d-none) column-range").forEach(function(element){
        element.addEventListener("change",function(e){
            column_name()
        })
    })

    let drop_down = this.querySelectorAll(".drop-down").forEach(function(element){
        element.addEventListener("click",function(e){
            if(element.classList.contains("fa-caret-right")){
                element.classList.remove("fa-caret-right");
                element.classList.add("fa-caret-down");
              document.querySelector("."+element.id).classList.remove("close");
            }
            else{
                element.classList.remove("fa-caret-down");
                element.classList.add("fa-caret-right");
                document.querySelector("."+element.id).classList.add("close");

            }
            console.log(element.id);
        })
    })
    
    function load(){
        document.querySelector(".modal-container").classList.remove("d-none");
        document.querySelector(".loader-container").classList.remove("d-none");
    }
    function stopLoad(){
        document.querySelector(".modal-container").classList.add("d-none");
        document.querySelector(".loader-container").classList.add("d-none");
    }
})
function column_name(table){
    
    var alphabet="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    var start =document.querySelector(".table-range:not(.d-none) .start-column-range").value;
    var end = document.querySelector(".table-range:not(.d-none) .end-column-range").value;    
    document.querySelector(".columns-name-container").innerHTML="";
    for(var i=start;i<=end;i++){
        document.querySelector(".columns-name-container").innerHTML+="<div class='row d-flex align-items-center px-4 my-2'>"+alphabet[i]+":<input class='form-control column-"+i+" ms-4 col-6' style='width:10rem;'></div>";
    }
    document.querySelector(".table-range:not(.d-none) .start-column-range").addEventListener("change",function(e){
        var start =document.querySelector(".table-range:not(.d-none) .start-column-range").value;
        var end = document.querySelector(".table-range:not(.d-none) .end-column-range").value;    
        document.querySelector(".columns-name-container").innerHTML="";
        for(var i=start;i<=end;i++){
            document.querySelector(".columns-name-container").innerHTML+="<div class='row d-flex align-items-center px-4 my-2'>"+alphabet[i]+":<input class='form-control column-"+i+" ms-4 col-6' style='width:10rem;'></div>";
        }
    })
    document.querySelector(".table-range:not(.d-none) .end-column-range").addEventListener("change",function(e){
        var start =document.querySelector(".table-range:not(.d-none) .start-column-range").value;
        var end = document.querySelector(".table-range:not(.d-none) .end-column-range").value;

        document.querySelector(".columns-name-container").innerHTML="";
        for(var i=start;i<=end;i++){
            document.querySelector(".columns-name-container").innerHTML+="<div class='row d-flex align-items-center px-4 my-2'>"+alphabet[i]+":<input class='form-control column-"+i+" ms-4 col-6' style='width:10rem;'></div>";
        }
    })  


}