document.addEventListener("DOMContentLoaded", () => {
    const table = document.getElementById('checkoutTable');
    const totalPriceElement = document.getElementById('totalPrice');
    const checkoutForm = document.getElementById('checkout-form');
    const tableBody = table.querySelector('tbody');

    const paymentMethodDropdown = document.getElementById('paymentMethod');
    const qrCodeContainer = document.getElementById('qr-code');

    // Pickup date and time elements
    const pickupDateInput = document.getElementById('pickupDate');
    const pickupTimeInput = document.getElementById('pickupTime');

    // Fetch cart data
    fetch('http://localhost:8080/viewCart')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.text();
        })
        .then(data => {
            const rows = data.trim().split('\n');
            let totalPrice = 0;

            tableBody.innerHTML = '';  // Clear the table before adding rows

            if (rows.length === 0 || (rows.length === 1 && rows[0].trim() === "")) {
                // If there are no items in the cart
                tableBody.innerHTML = "<tr><td colspan='3'>Your cart is empty.</td></tr>";
                totalPriceElement.textContent = "$0.00"; // Default to $0.00 if empty
                return;
            }

            rows.forEach(row => {
                if (row.trim() === "") return;
                const columns = row.split(',');

                const tr = document.createElement('tr');
                const tdName = document.createElement('td');
                tdName.textContent = columns[0]; // Product Name
                tr.appendChild(tdName);

                const tdPrice = document.createElement('td');
                const price = parseFloat(columns[1]);
                tdPrice.textContent = isNaN(price) ? "$0.00" : `$${price.toFixed(2)}`; // Price
                tr.appendChild(tdPrice);

                const tdQuantity = document.createElement('td');
                tdQuantity.textContent = columns[2]; // Quantity
                tr.appendChild(tdQuantity);

                tableBody.appendChild(tr);

                // Add price to total, ensuring it's valid and parsed correctly
                const itemQuantity = parseInt(columns[2]);
                if (!isNaN(price) && !isNaN(itemQuantity)) {
                    totalPrice += price * itemQuantity;
                }
            });

            // Display total price, ensuring it's not NaN
            totalPrice = isNaN(totalPrice) ? 0 : totalPrice;
            totalPriceElement.textContent = `${totalPrice.toFixed(2)}`;
        })
        .catch(error => {
            console.error('Error loading cart:', error);
            tableBody.innerHTML = "<tr><td colspan='3'>Failed to load cart.</td></tr>";
            totalPriceElement.textContent = "$0.00";  // Default total price if error
        });

    // Generate QR code based on payment method
    paymentMethodDropdown.addEventListener('change', function () {
        const selectedPaymentMethod = paymentMethodDropdown.value;
        generateQRCode(selectedPaymentMethod);
    });

    // Initial QR code generation if there's already a payment method selected
    generateQRCode(paymentMethodDropdown.value);

    // QR Code generation function
    function generateQRCode(paymentMethod) {
        // Clear previous QR code
        qrCodeContainer.innerHTML = '';

        // Define the DuitNow and TNG QR content (replace with actual URLs or data)
        let qrData = '';
        if (paymentMethod === 'duitnow') {
            qrData = 'https://example.com/duitnow-payment';  // Replace with actual DuitNow link or data
        } else if (paymentMethod === 'tng') {
            qrData = 'https://example.com/tng-payment';  // Replace with actual TNG link or data
        }

        // Generate and display the QR code
        if (qrData) {
            new QRCode(qrCodeContainer, {
                text: qrData,
                width: 128,
                height: 128,
                colorDark: "#000000",
                colorLight: "#ffffff",
                correctLevel: QRCode.CorrectLevel.H
            });
        } else {
            console.error("Invalid payment method for QR generation.");
        }
    }

    // Pickup date and time validation
    const today = new Date();
    const formattedToday = today.toISOString().split('T')[0]; // Format YYYY-MM-DD
    pickupDateInput.setAttribute('min', formattedToday); // Disable past dates

    // Restrict the pickup date to weekdays (Monday to Friday)
    pickupDateInput.addEventListener('input', function () {
        const selectedDate = new Date(pickupDateInput.value);
        const selectedDay = selectedDate.getDay(); // 0 - Sunday, 1 - Monday, ..., 6 - Saturday

        if (selectedDay === 0 || selectedDay === 6) { // Sunday or Saturday
            alert("We are closed on weekends. Please choose a weekday.");
            pickupDateInput.setCustomValidity("Pickup date must be Monday to Friday.");
        } else {
            pickupDateInput.setCustomValidity(""); // Reset error if valid
        }
    });

    // Restrict the pickup time to be between 10:00 AM and 10:00 PM
    pickupTimeInput.addEventListener('input', function () {
        const selectedTime = pickupTimeInput.value;
        const [hours, minutes] = selectedTime.split(':').map(Number);

        if (hours < 10 || hours >= 22) {
            alert("Pickup time must be between 10:00 AM and 10:00 PM.");
            pickupTimeInput.setCustomValidity("Invalid pickup time.");
        } else {
            pickupTimeInput.setCustomValidity(""); // Reset error if valid
        }
    });

    // Handle form submission
    checkoutForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const pickupDate = document.getElementById('pickupDate').value;
        const pickupTime = document.getElementById('pickupTime').value;
        const address = document.getElementById('address').value;
        const paymentReceipt = document.getElementById('paymentReceipt').files[0]; // Assuming file upload

        const formData = new FormData();
        formData.append("pickupDate", pickupDate);
        formData.append("pickupTime", pickupTime);
        formData.append("address", address);
        formData.append("paymentReceipt", paymentReceipt);

        fetch('http://localhost:8080/checkout', {
            method: 'POST',
            body: formData
        })
            .then(response => response.text())
            .then(data => {
                alert(data); // Show success message
                window.location.href = 'orderConfirmation.html'; // Redirect to confirmation page
            })
            .catch(error => {
                console.error('Error during checkout:', error);
            });
    });
});
