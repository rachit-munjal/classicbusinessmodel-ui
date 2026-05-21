// /**
//  * chart-init.js
//  * Reads `chartMonths` and `chartCounts` injected by Thymeleaf inline JS
//  * and renders a Chart.js bar chart in #ordersChart canvas.
//  */
// (function () {
//   const canvas = document.getElementById('ordersChart');
//   if (!canvas) return;
//
//   const ctx = canvas.getContext('2d');
//
//   new Chart(ctx, {
//     type: 'bar',
//     data: {
//       labels: typeof chartMonths !== 'undefined' ? chartMonths : [],
//       datasets: [{
//         data: typeof chartCounts !== 'undefined' ? chartCounts : [],
//         backgroundColor: '#444444',
//         hoverBackgroundColor: '#111111',
//         borderWidth: 0,
//         borderRadius: 0,
//       }]
//     },
//     options: {
//       responsive: true,
//       maintainAspectRatio: false,
//       plugins: {
//         legend: { display: false },
//         tooltip: {
//           backgroundColor: '#111',
//           titleColor: '#fff',
//           bodyColor: '#ccc',
//           padding: 10,
//           titleFont: { family: "'IBM Plex Mono', monospace", size: 11 },
//           bodyFont:  { family: "'IBM Plex Mono', monospace", size: 12 },
//           callbacks: {
//             title: (items) => items[0].label,
//             label: (item)  => ' Orders: ' + item.parsed.y,
//           }
//         }
//       },
//       scales: {
//         y: {
//           grid:  { color: 'rgba(0,0,0,0.07)', drawBorder: false },
//           ticks: {
//             color: '#888',
//             font:  { family: "'IBM Plex Mono', monospace", size: 10 },
//             maxTicksLimit: 6,
//           },
//           border: { display: false }
//         },
//         x: {
//           grid:  { display: false },
//           ticks: {
//             color: '#666',
//             font:  { family: "'IBM Plex Mono', monospace", size: 10 },
//           },
//           border: { display: false }
//         }
//       }
//     }
//   });
// })();




document.addEventListener('DOMContentLoaded', function () {

  const canvas = document.getElementById('ordersChart');

  if (!canvas) return;

  const ctx = canvas.getContext('2d');

  const months =
      typeof chartMonths !== 'undefined'
          ? chartMonths
          : [];

  const counts =
      typeof chartCounts !== 'undefined'
          ? chartCounts
          : [];

  new Chart(ctx, {

    type: 'line',

    data: {

      labels: months,

      datasets: [{

        label: 'Orders',

        data: counts,

        borderColor: '#36ADA3',

        backgroundColor: 'rgba(54, 173, 163, 0.12)',

        fill: true,

        tension: 0.4,

        pointRadius: 5,

        pointBackgroundColor: '#36ADA3',

        borderWidth: 3
      }]
    },

    options: {

      responsive: true,

      maintainAspectRatio: false,

      plugins: {

        legend: {
          display: false
        }
      },

      scales: {

        x: {

          ticks: {
            color: '#dbeafe'
          },

          grid: {
            color: 'rgba(255,255,255,0.05)'
          }
        },

        y: {

          ticks: {
            color: '#dbeafe'
          },

          grid: {
            color: 'rgba(255,255,255,0.05)'
          }
        }
      }
    }
  });

});