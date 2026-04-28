<script>
	import { resolve } from '$app/paths';
	import { cartStore } from '$lib/stores/cartStore.js';
	import StorefrontShell from '$lib/components/StorefrontShell.svelte';
	import { formatCurrency } from '$lib/utils/formatters.js';

	let message = $state('');

	async function checkout() {
		let items = [];

		const unsubscribe = cartStore.subscribe(value => {
			items = value;
		});
		unsubscribe();

		const auth = JSON.parse(localStorage.getItem('auth') ?? '{}');

		const res = await fetch('http://localhost:8080/api/orders', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
				Authorization: 'Basic ' + btoa(`${auth.email}:${auth.password}`)
			},
			body: JSON.stringify({
				items: items.map(item => ({
					productId: item.id,
					quantity: item.quantity
				}))
			})
		});

		if (!res.ok) {
			message = 'Checkout failed. Please check your stock or connection.';
			return;
		}

		cartStore.clear();
		message = 'Order placed successfully!';
	}
</script>

<svelte:head>
	<title>Your Cart</title>
</svelte:head>

<StorefrontShell>
	<div class="space-y-8">
		<header>
			<p class="text-sm font-semibold tracking-[0.25em] text-slate-500 uppercase">Checkout</p>
			<h1 class="mt-2 text-4xl font-black tracking-tight text-slate-950">Shopping Cart</h1>
		</header>

		{#if $cartStore.length === 0}
			<div class="rounded-[2rem] border border-slate-200 bg-white p-12 text-center shadow-sm">
				<h2 class="text-2xl font-bold text-slate-950">Your cart is empty.</h2>
				<p class="mt-2 text-slate-600">Looks like you haven't added anything yet.</p>
				<a class="cta-button mt-8 inline-flex" href={resolve('/products')}>Start shopping</a>
			</div>
		{:else}
			<div class="grid gap-8 lg:grid-cols-[1fr,380px]">
				<div class="space-y-4">
					{#each $cartStore as item (item.id)}
						<div class="flex items-center gap-6 rounded-[2rem] border border-slate-200 bg-white p-6 shadow-sm">
							<div class="flex h-20 w-20 flex-shrink-0 items-center justify-center rounded-2xl bg-slate-100">
								<span class="text-2xl font-bold text-slate-300">{item.name[0]}</span>
							</div>
							<div class="flex-grow">
								<h2 class="text-lg font-bold text-slate-950">{item.name}</h2>
								<p class="text-sm text-slate-500">{item.categoryName}</p>
								<div class="mt-2 flex items-center gap-4">
									<span class="font-bold text-slate-950">{formatCurrency(item.price)}</span>
									<span class="text-sm text-slate-400">Qty: {item.quantity}</span>
								</div>
							</div>
							<button
								type="button"
								onclick={() => cartStore.remove(item.id)}
								class="rounded-xl p-2 text-slate-400 transition hover:bg-red-50 hover:text-red-600"
							>
								<span class="sr-only">Remove</span>
								<svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
									<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
								</svg>
							</button>
						</div>
					{/each}
				</div>

				<aside class="h-fit space-y-6 rounded-[2rem] border border-slate-200 bg-white p-8 shadow-sm">
					<h2 class="text-xl font-bold text-slate-950">Order Summary</h2>
					<div class="space-y-3 text-sm border-b border-slate-100 pb-6">
						<div class="flex justify-between">
							<span class="text-slate-600">Subtotal</span>
							<span class="font-semibold text-slate-950">
								{formatCurrency($cartStore.reduce((acc, item) => acc + item.price * item.quantity, 0))}
							</span>
						</div>
						<div class="flex justify-between">
							<span class="text-slate-600">Shipping</span>
							<span class="font-semibold text-emerald-600">Free</span>
						</div>
					</div>
					<div class="flex justify-between text-lg font-bold text-slate-950">
						<span>Total</span>
						<span>
							{formatCurrency($cartStore.reduce((acc, item) => acc + item.price * item.quantity, 0))}
						</span>
					</div>
					<button
						type="button"
						onclick={checkout}
						class="cta-button w-full"
					>
						Place Order
					</button>
					<p class="text-center text-xs text-slate-400">
						By placing your order, you agree to Team 26's terms of service and privacy policy.
					</p>
				</aside>
			</div>
		{/if}

		{#if message}
			<div class:bg-emerald-50={message.includes('successfully')} class:border-emerald-200={message.includes('successfully')} class:text-emerald-700={message.includes('successfully')} class:bg-red-50={!message.includes('successfully')} class:border-red-200={!message.includes('successfully')} class:text-red-700={!message.includes('successfully')} class="rounded-2xl border p-4 text-center font-bold">
				{message}
				{#if message.includes('successfully')}
					<div class="mt-4">
						<a href={resolve('/orders')} class="cta-button inline-flex">View My Orders</a>
					</div>
				{/if}
			</div>
		{/if}
	</div>
</StorefrontShell>