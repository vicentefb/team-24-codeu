

google.charts.load('current', { packages: ['corechart'] });
google.charts.setOnLoadCallback(drawDonutChart);

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
        title: 'My Weekly Commute Data',
        width: 800,
        height: 800,
        pieHole: 0.4,
        colors: slices_color,
    };

    //create an instance of the type of chart (bar here)
    var chart = new google.visualization.PieChart(document.getElementById('weekly_donut_chart'));

    //draw the chart
    chart.draw(weekly_donut_data, chart_options);
}