<script>
	let orders = $state([]);
	let error = $state('');

	function money(value) {
		return new Intl.NumberFormat('en-US', {
			style: 'currency',
			currency: 'USD'
		}).format(Number(value ?? 0));
	}

	function niceDate(value) {
		return new Date(value).toLocaleString();
	}

	async function loadOrders() {
		const auth = JSON.parse(localStorage.getItem('auth') ?? '{}');

		try {
			const res = await fetch('http://localhost:8080/api/me/orders', {
				headers: {
					Authorization: 'Basic ' + btoa(`${auth.email}:${auth.password}`)
				}
			});

			if (!res.ok) {
				error = 'Failed to load orders';
				return;
			}

			orders = await res.json();
		} catch {
			error = 'Error loading orders';
		}
	}

	loadOrders();
</script>

<div class="mx-auto max-w-5xl px-6 py-8">
	<div class="mb-8 flex items-center justify-between">
		<h1 class="text-4xl font-bold">My Orders</h1>

		<a href="/products" class="rounded-xl bg-gray-200 px-4 py-2 font-semibold text-slate-800">
			← Back to Products
		</a>
	</div>

	{#if error}
		<p class="rounded-xl bg-red-100 p-4 font-semibold text-red-700">{error}</p>
	{:else if orders.length === 0}
		<p>No orders yet.</p>
	{:else}
		<div class="space-y-6">
			{#each orders as order}
				<div class="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
					<div class="mb-4 flex items-start justify-between">
						<div>
							<h2 class="text-2xl font-bold">Order #{order.id}</h2>
							<p class="text-sm text-slate-500">{niceDate(order.date)}</p>
						</div>

						<span class="rounded-full bg-green-100 px-3 py-1 text-sm font-bold text-green-700">
							{order.status}
						</span>
					</div>

					<div class="mb-4 grid gap-3 sm:grid-cols-2">
						<div class="rounded-xl bg-slate-50 p-4">
							<p class="text-sm text-slate-500">Shipping Address</p>
							<p class="font-semibold">{order.shippingAddress}</p>
						</div>

						<div class="rounded-xl bg-slate-50 p-4">
							<p class="text-sm text-slate-500">Total</p>
							<p class="text-xl font-bold">{money(order.total)}</p>
						</div>
					</div>

					<h3 class="mb-2 font-bold">Items</h3>

					<div class="space-y-2">
						{#each order.items as item}
							<div class="flex items-center justify-between rounded-xl border border-slate-200 p-4">
								<div>
									<p class="font-bold">{item.productName}</p>
									<p class="text-sm text-slate-500">Product ID: {item.productId}</p>
								</div>

								<div class="text-right">
									<p class="font-semibold">Qty: {item.quantity}</p>
									<p class="text-sm text-slate-500">
										{money(item.priceAtPurchase)} each
									</p>
								</div>
							</div>
						{/each}
					</div>
				</div>
			{/each}
		</div>
	{/if}
</div>