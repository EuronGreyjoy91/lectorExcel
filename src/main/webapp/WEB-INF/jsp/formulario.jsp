<%-- 
    Document   : cabecera
    Created on : 22/09/2018, 06:24:37 PM
    Author     : Federico
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang = "es">
    <head>
        <meta charset="utf-8">
   		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
   		<link rel="icon" href="icon.ico" type="image/x-icon" />
   		<link href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
       	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
        <title>Lector de Excel - FB</title> 
        <style>
        	.main-container{
        		margin-top: 40px;
        	}
        </style> 
        <script>
			// Example starter JavaScript for disabling form submissions if there are invalid fields
			(function() {
			  'use strict';
			  window.addEventListener('load', function() {
			    // Fetch all the forms we want to apply custom Bootstrap validation styles to
			    var forms = document.getElementsByClassName('needs-validation');
			    // Loop over them and prevent submission
			    var validation = Array.prototype.filter.call(forms, function(form) {
			      form.addEventListener('submit', function(event) {
			        if (form.checkValidity() === false) {
			          event.preventDefault();
			          event.stopPropagation();
			        }
			        form.classList.add('was-validated');
			      }, false);
			    });
			  }, false);
			})();
		</script>
    </head>
    <body>
    	<div class = "container main-container">
    		<h1>Procesador de excel de Facebook <small>v1.1.0</small></h1>
    		<form action = "/procesar" class="needs-validation" novalidate enctype="multipart/form-data" method = "POST">
			  <div class="form-row">
			    <div class="col-md-12 mb-3">
			      <label for="file"><b>Archivo</b></label>
			      <input type="file" class="form-control" id="file" name = "file" required>
			      <div class="valid-feedback">
			        Perfecto!
			      </div>
			      <div class="invalid-feedback">
			        Carga un archivo!
			      </div>
			    </div>
			  </div>
			  <div class="form-row">
			    <div class="col-md-12 mb-3">
			      <label for="mail"><b>Email l&iacute;mite</b></label>
			      <input type="email" class="form-control" id="mail" name = "mail">
			    </div>
			    <button type="submit" class="btn btn-outline-primary" style = "margin-top: 10px"><i class="fa fa-check" aria-hidden="true"></i>&nbsp;Procesar</button>
			  	<button type="reset" class="btn btn-outline-danger ml-3" style = "margin-top: 10px"><i class="fa fa-times" aria-hidden="true"></i>&nbsp;Limpiar</button>		 
			  </div>
			</form>
				<a href = "https://www.youtube.com/watch?v=RZIhpba83hY" target = "_blank">
					<div data-toggle="tooltip" data-placement="left" title="?????" 
					style = "position:absolute; right: 5px; bottom:10px;background-color:green;width:13px;height:13px"></div>
				</a>
    	</div>
 	</body>
 	<script>
 		$('[data-toggle="tooltip"]').tooltip()
 	</script>
</html>