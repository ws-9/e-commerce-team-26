<script>
	let { product } = $props();

	let message = $state('');
	let isSuccess = $state(false);
	let stock = $state(product.stockQty ?? product.stock_qty ?? 0);

	async function placeOrder() {
		const auth = JSON.parse(localStorage.getItem('auth') ?? '{}');

		try {
			const res = await fetch('http://localhost:8080/api/orders', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
					Authorization: 'Basic ' + btoa(`${auth.email}:${auth.password}`)
				},
				body: JSON.stringify({
					items: [{ productId: product.id, quantity: 1 }]
				})
			});

			if (!res.ok) {
				isSuccess = false;
				message = 'Order failed';
				return;
			}

			stock = Math.max(stock - 1, 0);
			isSuccess = true;
			message = 'Order placed successfully!';
		} catch {
			isSuccess = false;
			message = 'Order failed';
		}
	}
</script>

<div class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
	<h3 class="text-lg font-bold">{product.name}</h3>
	<p class="text-sm text-gray-600">{product.description}</p>
	<p class="mt-2 font-bold">${product.price}</p>
	<p class="text-sm text-gray-500">Stock: {stock}</p>

	<button
		type="button"
		on:click={placeOrder}
		class="mt-4 rounded-xl bg-blue-600 px-4 py-2 font-bold text-white"
	>
		Order
	</button>

	{#if message}
		<p class="mt-3 rounded-lg p-2 text-sm font-semibold {isSuccess ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}">
			{message}
		</p>
	{/if}
</div>