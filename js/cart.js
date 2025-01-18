document.addEventListener("DOMContentLoaded", () => {
    const table = document.getElementById('cartTable');

    // Ensure the table exists in the DOM
    if (!table) {
        console.error("Error: Table with id 'cartTable' not found in the DOM!");
        return;
    }

    // Ensure the table has a <tbody> element
    let tableBody = table.querySelector('tbody');
    if (!tableBody) {
        console.error("Error: <tbody> not found in cartTable!");
        // If <tbody> doesn't exist, create it
        tableBody = document.createElement('tbody');
        table.appendChild(tableBody);
    }

    // Fetch cart data from the backend
    fetch('http://localhost:8080/viewCart')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.text();
        })
        .then(data => {
            const rows = data.trim().split('\n'); // Split the response by lines

            // Clear the table body before adding new rows
            tableBody.innerHTML = '';

            rows.forEach(row => {
                if (row.trim() === "") return; // Skip empty lines

                const columns = row.split(','); // Split row into columns
                const tr = document.createElement('tr');

                // Add product name
                const tdName = document.createElement('td');
                tdName.textContent = columns[0]; // First column: product name
                tr.appendChild(tdName);

                // Add price
                const tdPrice = document.createElement('td');
                tdPrice.textContent = columns[1]; // Second column: price
                tr.appendChild(tdPrice);

                // Add quantity
                const tdQuantity = document.createElement('td');
                tdQuantity.textContent = columns[2]; // Third column: quantity
                tr.appendChild(tdQuantity);

                // Append the row to the table body
                tableBody.appendChild(tr);
            });
        })
        .catch(error => console.error('Error loading cart:', error));
});
