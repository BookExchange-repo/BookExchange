$('.ui.accordion')
  .accordion({
    exclusive: false
});

$('.ui.dropdown')
  .dropdown("set selected", 2);

$('.ui.search').keypress(function(e){
  if (e.which == 13) {
    $(location).attr('href', 'search.html');
  }
});
