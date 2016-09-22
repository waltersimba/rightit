// Function to get window width
$.fn.exist = function(){ return $(this).length > 0; }
function get_width() {
	return $(window).width();
}

// Function to set color
function set_color() {
	var color = localStorage.getItem('color');
	$('#color-chooser').val(color);
	$('#color-style').attr('href','css/style-'+color+'.css');
	$('.logo img').attr('src','images/logo-'+color+'.png');
}


$(function(){

	// Change Color Style
    $('.chooser-toggle').click(function(){
    	$('.chooser').toggleClass('chooser-hide');
    })
    if (localStorage.getItem('color') == null) {
    	localStorage.setItem('color', 'blue');
    }
    set_color();
	$('#color-chooser').change(function(){
		localStorage.setItem('color', $(this).val());
		set_color();
	});

	// Select2 for styling select element
	$('.select2').select2();

	// open navigation dropdown on hover (only when width >= 768px)
	$('ul.nav li.dropdown').hover(function() {
		if (get_width() >= 768) {
			$(this).addClass('open');
		}
	}, function() {
		if (get_width() >= 768) {
			$(this).removeClass('open');
		}
	});

	// owlCarousel for Home Slider
	if ($('.home-slider').exist()) {
		$('.home-slider').owlCarousel({
			items:1,
		    loop:true,
		    autoplay:true,
		    autoplayHoverPause:true,
		    dots:false,
		    nav:true,
		    navText:['<i class="fa fa-angle-left"></i>','<i class="fa fa-angle-right"></i>'],
		});
	}

	// owlCarousel for Widget Slider
	if ($('.widget-slider').exist()) {
		var widget_slider = $('.widget-slider');
		widget_slider.owlCarousel({
		    loop:true,
		    dots:false,
		    nav:true,
		    navText:['<i class="fa fa-angle-left"></i>','<i class="fa fa-angle-right"></i>'],
		    responsive:{
		        0:{
		        	items:1,
		        },
		        768:{
		        	items:3,
		        	margin:15
		        },
		        992:{
		        	items:1,
		        }
		    }
		});
		widget_slider.on('changed.owl.carousel', function(event) {
			$('button[data-toggle="tooltip"]').tooltip({container:'body'});
			$('a[data-toggle="tooltip"]').tooltip({container:'body'});
		});
	}

	// Tooltip
	$('button[data-toggle="tooltip"]').tooltip({container:'body'});
	$('a[data-toggle="tooltip"]').tooltip({container:'body'});

	// Touchspin
	if ($('.input-qty').exist()) {
		$('.input-qty input').TouchSpin();
	}

	// Back top Top
    $(window).scroll(function(){
		if ($(this).scrollTop()>70) {
			$('.back-top').fadeIn();
		} else {
			$('.back-top').fadeOut();
		}
	});
});
