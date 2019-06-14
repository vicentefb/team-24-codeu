
google.charts.load('current', { 'packages': ['corechart', 'map'] });

//loads the charts that will be created
google.charts.setOnLoadCallback(drawDonutChart);
google.charts.setOnLoadCallback(drawBarGraph);
google.charts.setOnLoadCallback(drawMilesCounterDonut);

/*
 * 
 * the function that draws the donut chart
 * 
 */ 
function drawDonutChart() {
    //instance of DataTable for the chart
    var weekly_donut_data = google.visualization.arrayToDataTable([
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
 * the function that draws the miles counter donut chart
 * 
 */
function drawMilesCounterDonut() {
    //instance of DataTable for the chart
    var totalSustainableMiles = 8.5; //FIXME: replace with actual data
    var totalMilesGoal = 15; //will be hard coded now, they can set goal in V2
    var restOfPie = totalMilesGoal - totalSustainableMiles;
    if (restOfPie < 0) {
        restOfPie = 0;
    }

    var weekly_miles_data = google.visualization.arrayToDataTable([
        ['Miles Stats', 'Miles Traveled Sustainably'],
        ['', totalSustainableMiles], //replace with actual
        ['', restOfPie],
    ]);

    //dictionary of customization for the chart
    var miles_circle_options = {
        pieHole: 0.6,
        pieSliceText: 'none',
        //tooltip: { trigger: 'none' },
        slices: {
            0: { color: '#5cd65c' },
            1: { color: 'transparent' }
        },
        legend: {
            position: 'none'
        },

    };

    //create an instance of the type of chart (bar here)
    var miles_chart = new google.visualization.PieChart(document.getElementById('weekly_miles_chart'));

    //draw the chart
    miles_chart.draw(weekly_miles_data, miles_circle_options);
}


