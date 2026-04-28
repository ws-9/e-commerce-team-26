<script>
	import StorefrontShell from '$lib/components/StorefrontShell.svelte';
	import { formatCurrency } from '$lib/utils/formatters.js';

	let orders = $state([]);
	let error = $state('');

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
				error = 'Failed to load orders. Please try again later.';
				return;
			}

			orders = await res.json();
		} catch {
			error = 'Error loading orders. Connection failed.';
		}
	}

	loadOrders();
</script>

<svelte:head>
	<title>My Orders</title>
</svelte:head>

<StorefrontShell>
	<div class="space-y-8">
		<header>
			<p class="text-sm font-semibold tracking-[0.25em] text-slate-500 uppercase">History</p>
			<h1 class="mt-2 text-4xl font-black tracking-tight text-slate-950">My Orders</h1>
		</header>

		{#if error}
			<div class="rounded-2xl border border-red-200 bg-red-50 p-6 text-red-700">
				<p class="font-bold">{error}</p>
			</div>
		{:else if orders.length === 0}
			<div class="rounded-[2rem] border border-slate-200 bg-white p-12 text-center shadow-sm">
				<h2 class="text-2xl font-bold text-slate-950">No orders yet.</h2>
				<p class="mt-2 text-slate-600">Your order history will appear here once you make a purchase.</p>
			</div>
		{:else}
			<div class="space-y-6">
				{#each orders as order (order.id)}
					<div class="rounded-[2rem] border border-slate-200 bg-white p-8 shadow-sm">
						<div class="mb-6 flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between">
							<div>
								<h2 class="text-2xl font-black tracking-tight text-slate-950">Order #{order.id}</h2>
								<p class="text-sm font-medium text-slate-500">{niceDate(order.date)}</p>
							</div>

							<span class="inline-flex rounded-full bg-emerald-100 px-4 py-1.5 text-xs font-black tracking-wider text-emerald-700 uppercase">
								{order.status}
							</span>
						</div>

						<div class="mb-8 grid gap-4 sm:grid-cols-2">
							<div class="rounded-2xl bg-slate-50 p-5">
								<p class="text-xs font-bold tracking-wider text-slate-400 uppercase">Shipping Address</p>
								<p class="mt-2 text-sm font-semibold text-slate-700">{order.shippingAddress}</p>
							</div>

							<div class="rounded-2xl bg-slate-950 p-5 text-white">
								<p class="text-xs font-bold tracking-wider text-slate-400 uppercase">Order Total</p>
								<p class="mt-2 text-2xl font-black">{formatCurrency(order.total)}</p>
							</div>
						</div>

						<h3 class="mb-4 text-sm font-bold tracking-wider text-slate-500 uppercase">Items</h3>

						<div class="space-y-3">
							{#each order.items as item}
								<div class="flex items-center justify-between rounded-2xl border border-slate-100 p-5 transition hover:border-slate-200">
									<div class="flex items-center gap-4">
										<div class="flex h-12 w-12 items-center justify-center rounded-xl bg-slate-50 text-lg font-bold text-slate-300">
											{item.productName[0]}
										</div>
										<div>
											<p class="font-bold text-slate-950">{item.productName}</p>
											<p class="text-xs text-slate-400">ID: {item.productId}</p>
										</div>
									</div>

									<div class="text-right">
										<p class="text-sm font-bold text-slate-950">Qty: {item.quantity}</p>
										<p class="text-xs text-slate-500">
											{formatCurrency(item.priceAtPurchase)} each
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
</StorefrontShell>