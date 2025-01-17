document.addEventListener("DOMContentLoaded", () => {
    const productList = document.getElementById('product-list');
    fetch('/products')
        .then(response => response.json())
        .then(data => {
            data.forEach(product => {
                const productDiv = document.createElement('div');
                productDiv.className = 'product';
                productDiv.innerHTML = `
                    <img src="images/${product.image}" alt="${product.name}">
                    <h3>${product.name}</h3>
                    <p>Price: $${product.price.toFixed(2)}</p>
                    <button onclick="addToCart(${product.id})">Add to Cart</button>
                `;
                productList.appendChild(productDiv);
            });
        });
});

function addToCart(productId) {
    fetch(`/cart/add/${productId}`, { method: 'POST' })
        .then(() => alert('Product added to cart!'))
        .catch(() => alert('Error adding product to cart.'));
}
