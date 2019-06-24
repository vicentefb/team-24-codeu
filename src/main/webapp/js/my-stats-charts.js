
google.charts.load('current', { 'packages': ['corechart', 'map'] });

//loads the charts that will be created
google.charts.setOnLoadCallback(drawDonutChart);
google.charts.setOnLoadCallback(drawBarGraph);
google.charts.setOnLoadCallback(drawMilesCounterDonut);
google.charts.setOnLoadCallback(drawHoursCounterDonut);
google.charts.setOnLoadCallback(drawFavModeDonut);
google.charts.setOnLoadCallback(drawChalsWonDonut);

/*
 * the function that draws the donut chart
 */ 
function drawDonutChart() {
    fetch("/modesOfTransp")
    .then((response) => {
        return response.json();
    })
    .then((modesJson) => {

        var weekly_donut_data = google.visualization.arrayToDataTable([
            ['Commute Method', 'Miles per Week', { type: 'string', role: 'tooltip' }],
            [modesJson[0].name, modesJson[0].miles, modesJson[0].name + ": " + modesJson[0].miles + " miles"],
            [modesJson[1].name, modesJson[1].miles, modesJson[1].name + ": " + modesJson[1].miles + " miles"],
            [modesJson[2].name, modesJson[2].miles, modesJson[2].name + ": " + modesJson[2].miles + " miles"],
            [modesJson[3].name, modesJson[3].miles, modesJson[3].name + ": " + modesJson[3].miles + " miles"],
            [modesJson[4].name, modesJson[4].miles, modesJson[4].name + ": " + modesJson[4].miles + " miles"],
            [modesJson[5].name, modesJson[5].miles, modesJson[5].name + ": " + modesJson[5].miles + " miles"]
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
            title: 'How I Traveled This Week (Miles)',
            titleTextStyle: { 
                fontSize: 16,
                bold: false,
                italic: true 
            }
        };
        
        //create an instance of the type of chart
        var chart = new google.visualization.PieChart(document.getElementById('weekly_donut_chart'));
        //draw the chart
        chart.draw(weekly_donut_data, chart_options);
    });
}

/*
 * the function that draws the emissions bar graph
 */ 
function drawBarGraph() {
    fetch("/emissions")
    .then((response) => {
        return response.json();
    })
    .then((emissionsJson) => {
        var my_emiss_color;
        
        /* if my emissions is greater than the average, my bar is red and average is red,
         * otherwise, mine is green and average is red
         */
        if(emissionsJson[0] < emissionsJson[1]) {
            my_emiss_color = '#5cd65c';
        } else {
            my_emiss_color = '#ff3333';
        }

        var weekly_emissions_data = google.visualization.arrayToDataTable([
            ['Source', 'Kilograms of CO2', {role: 'style'}],
            ['My Emissions', emissionsJson[0], my_emiss_color],
            ['Average Emissions', emissionsJson[1], '#ff3333']
        ]);

        var emissions_axis_size = 16;

        //dictionary of customization for the chart
        var graph_options = {
            vAxis: {
                title: 'Kilograms of CO2',
                titleFontSize: emissions_axis_size,
            },
            hAxis: {
                title: 'Source of Emissions',
                titleFontSize: emissions_axis_size,
            },
            legend: {
                position: 'none'
            }
        };

        //create an instance of the type of chart
        var graph = new google.visualization.ColumnChart(document.getElementById('weekly_bar_graph'));
        //draw the chart
        graph.draw(weekly_emissions_data, graph_options);
    });
}

/*
 * the function that draws the miles counter donut chart
 */
function drawMilesCounterDonut() {
    fetch("/miniStats")
    .then((response) => {
        return response.json();
    })
    .then((miniStatsJson) => {
        var totalSustainableMiles = miniStatsJson[0].totalMiles;
        var totalMilesGoal = miniStatsJson[0].totalMilesGoal;
        var restOfPie = totalMilesGoal - totalSustainableMiles;
        if (restOfPie < 0) {
            restOfPie = 0;
        }

        var milesText = "My Sustainable Miles: " + totalSustainableMiles + " mi";
        var milesGoalText = "Miles to Reach My Goal: " + restOfPie + " mi";
    
        var weekly_miles_data = google.visualization.arrayToDataTable([
            ['Miles Stats', 'Miles Traveled Sustainably', { type: 'string', role: 'tooltip' }],
            ['', totalSustainableMiles, milesText], //replace with actual
            ['', restOfPie, milesGoalText],
        ]);

        //dictionary of customization for the chart
        var miles_circle_options = {
            pieHole: 0.6,
            pieSliceText: 'none',
            slices: {
                0: { color: '#5cd65c' },
                1: { color: '#e6e6e6' }
            },
            legend: {
                position: 'none'
            }
        };

        //create an instance of the type of chart
        var miles_chart = new google.visualization.PieChart(document.getElementById('weekly_miles_chart'));
        //draw the chart
        miles_chart.draw(weekly_miles_data, miles_circle_options);
    });
}

/*
 * the function that draws the hours counter donut chart
 */
function drawHoursCounterDonut() {
    fetch("/miniStats")
    .then((response) => {
        return response.json();
    })
    .then((miniStatsJson) => {
        var totalSustainableHours = miniStatsJson[1].totalHours;
        var totalHoursGoal = miniStatsJson[1].totalHoursGoal;
        var restOfPie = totalHoursGoal - totalSustainableHours;
        if (restOfPie < 0) {
            restOfPie = 0;
        }

        var hoursText = "My Sustainable Hours: " + totalSustainableHours + " hrs";
        var hoursGoalText = "Hours to Reach My Goal: " + restOfPie + " hrs";
    
        var weekly_hours_data = google.visualization.arrayToDataTable([
            ['Hours Stats', 'Hours Traveled Sustainably', { type: 'string', role: 'tooltip' }],
            ['', totalSustainableHours, hoursText], 
            ['', restOfPie, hoursGoalText],
        ]);

        //dictionary of customization for the chart
        var hours_circle_options = {
            pieHole: 0.6,
            pieSliceText: 'none',
            slices: {
                0: { color: '#5cd65c' },
                1: { color: '#e6e6e6' }
            },
            legend: {
                position: 'none'
            },
        };

        //create an instance of the type of chart (bar here)
        var hours_chart = new google.visualization.PieChart(document.getElementById('weekly_hours_chart'));
        //draw the chart
        hours_chart.draw(weekly_hours_data, hours_circle_options);
    });
}

/*
 * the function that draws the favorite mode donut chart
 */
function drawFavModeDonut() {
    fetch("/miniStats")
    .then((response) => {
        return response.json();
    })
    .then((miniStatsJson) => {
        var favoriteMode = miniStatsJson[2].favMode;
        var favModeMiles = miniStatsJson[2].favModeMiles;
    
        var favModeText = "Favorite Mode: " + favoriteMode + ", " + favModeMiles + " mi";

        var weekly_fav_mode_data = google.visualization.arrayToDataTable([
            ['Mode of Transportation', 'Miles Traveled', { type: 'string', role: 'tooltip' }],
            [favoriteMode, favModeMiles, favModeText], 
        ]);
    
        //dictionary of customization for the chart
        var fav_mode_circle_options = {
            pieHole: 0.6,
            pieSliceText: 'none',
            slices: {
                0: { color: '#5cd65c' }
            },
            legend: {
                position: 'none'
            }
        };

        //sets correct icon image that appears below the chart based on the mode
        switch(favoriteMode) {
            case "Bike": 
                document.getElementById('fav_mode_icon').src="/css/png/005-bicycle.png";
                break;
            case "Walk": 
                document.getElementById("fav_mode_icon").src="/css/png/013-walk.png";
                break;
            case "Car (Carpool)": 
                document.getElementById("fav_mode_icon").src="/css/png/001-key.png";
                break;
            case "Car (Single Rider)": 
                document.getElementById("fav_mode_icon").src="/css/png/001-key.png";
                break;
            case "Scooter": 
                document.getElementById("fav_mode_icon").src="/css/png/004-scooter.png";
                break; 
            case "Rollerblade": 
                document.getElementById("fav_mode_icon").src="/css/png/002-roller-skate.png";
                break;        
            default:
                document.getElementById("fav_mode_icon").src="/css/png/011-like.png";
                break;
        }
    
        //create an instance of the type of chart
        var fav_mode_chart = new google.visualization.PieChart(document.getElementById('weekly_fav_mode_chart'));
        //draw the chart
        fav_mode_chart.draw(weekly_fav_mode_data, fav_mode_circle_options);
    });
}

/*
 * the function that draws the challenges won donut chart
 */
function drawChalsWonDonut() {
    fetch("/miniStats")
    .then((response) => {
        return response.json();
    })
    .then((miniStatsJson) => {
        var totalChalsWon = miniStatsJson[3].totalChallengesWon;
        var totalChalsWonGoal = miniStatsJson[3].totalChallengesGoal;
        var restOfPie = totalChalsWonGoal - totalChalsWon;
        if (restOfPie < 0) {
            restOfPie = 0;
        }

        var chalsText = "My Challenges Won: " + totalChalsWon;
        var chalsGoalText = "Challenges to Reach My Goal: " + restOfPie;
    
        var weekly_chals_won_data = google.visualization.arrayToDataTable([
            ['Challenges Won Stats', 'Challenges Won', { type: 'string', role: 'tooltip' }],
            ['', totalChalsWon, chalsText],
            ['', restOfPie, chalsGoalText],
        ]);
    
        //dictionary of customization for the chart
        var chals_won_circle_options = {
            pieHole: 0.6,
            pieSliceText: 'none',
            slices: {
                0: { color: '#5cd65c' },
                1: { color: '#e6e6e6' }
            },
            legend: {
                position: 'none'
            },
            tooltip: {
                1: { trigger: 'none' }, //FIXME: how to only allow the green section to be hoverable?
            }
        };
    
        //create an instance of the type of chart (bar here)
        var chals_won_chart = new google.visualization.PieChart(document.getElementById('weekly_chals_won_chart'));
        //draw the chart
        chals_won_chart.draw(weekly_chals_won_data, chals_won_circle_options);
    })
}