
google.charts.load('current', { packages: ['corechart'] });

//loads the charts that will be created
google.charts.setOnLoadCallback(drawDonutChart);
google.charts.setOnLoadCallback(drawBarGraph);

/*
 * 
 * the function that draws the donut chart
 * 
 */ 
function drawDonutChart() {
    //instance of DataTable for the chart
    var weekly_donut_data = new google.visualization.arrayToDataTable([
        ['Commute Method', 'Hours per Week'],
        ['Bike', 2.75],
        ['Scooter', 0.5],
        ['Walk', 2],
        ['Rollerblade', 0.75],
        ['Car (Single Rider)', 1.25],
        ['Car (Carpool)', 2]
    ]);

    //sorts the data from fewest hours per week to most
    weekly_donut_data.sort([{ column: 1 }]);

    //array of green colors from light to dark
    var green_shades = ['#aeeaae', '#85e085', '#5cd65c', '#2eb82e', '#248f24'];

    //iterates through the data and assigns a color to the chart based on hours value
    var slices_color = [];
    var green_index = 0;
    for (var i = 0; i < weekly_donut_data.getNumberOfRows(); i++) {
        if (weekly_donut_data.getValue(i, 0) == 'Car (Single Rider)') {
            slices_color.push('#ff3333');
        } else {
            slices_color.push(green_shades[green_index]);
            green_index++;
        }
    }

    //dictionary of customization for the chart
    var chart_options = {
        pieHole: 0.3,
        colors: slices_color,
    };

    //create an instance of the type of chart (bar here)
    var chart = new google.visualization.PieChart(document.getElementById('weekly_donut_chart'));

    //draw the chart
    chart.draw(weekly_donut_data, chart_options);
}
/*
 * 
 * the function that draws the bar graph
 * 
 */ 
function drawBarGraph() {
    var weekly_bar_data = google.visualization.arrayToDataTable([
        ['Source', 'Kilograms of CO2', { role: 'style' }],
        ['My Emissions', 45, '#3366cc'], //emissions will be calculated by multiplying miles * .404
        ['Average Emissions', 88, '#3366cc'], //hardcoded data, will not change
    ]);

    var graph_options = {
  
    };

    var graph = new google.visualization.ColumnChart(document.getElementById('weekly_bar_graph'));

    graph.draw(weekly_bar_data, graph_options);
}
/*
 * 
 * the function that determines the size of the stats circles
 * 
 */
function calcCircleSizes() {
    var sustainable_miles_week = 15; //FIXME: replace with actual data
    var total_miles_week = 35; //FIXME: replace with actual data
    var miles_circle_size = (weekly_miles / total_miles_week) * 100;

    document.getElementById('miles_circle').style.height = miles_circle_size;
    document.getElementById('miles_circle').style.width = miles_circle_size;
    document.getElementById('miles_circle').textContent(' miles');

    //miles_circle.setAttribute("height", miles_circle_size);
    //miles_circle.setAttribute("width", miles_circle_size);
    //miles_circle.textContent(sustainable_miles_week + ' miles');
    //miles_circle.textContent(' miles');
}
