<script>
	import { cartStore } from '$lib/stores/cartStore.js';

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
			message = 'Checkout failed';
			return;
		}

		cartStore.clear();
		message = 'Order placed successfully!';
	}
</script>

<h1 class="mb-6 text-3xl font-bold">Shopping Cart</h1>
<a
	href="/products"
	class="inline-block mt-2 mb-6 rounded-xl bg-gray-200 px-4 py-2 font-semibold text-slate-800"
>
	← Back to Products
</a>
{#if $cartStore.length === 0}
	<p>Your cart is empty.</p>
{:else}
	<div class="space-y-4">
		{#each $cartStore as item}
			<div class="rounded-xl border bg-white p-4">
				<h2 class="font-bold">{item.name}</h2>
				<p>${item.price}</p>
				<p>Quantity: {item.quantity}</p>

				<button
					type="button"
					on:click={() => cartStore.remove(item.id)}
					class="mt-2 rounded bg-red-600 px-3 py-1 text-white"
				>
					Remove
				</button>
			</div>
		{/each}
	</div>

	<button
		type="button"
		on:click={checkout}
		class="mt-6 rounded-xl bg-blue-600 px-5 py-3 font-bold text-white"
	>
		Checkout
	</button>
{/if}
{#if message}
	<p class="mt-4 font-semibold">{message}</p>

	{#if message.includes('success')}
		<a
			href="/orders"
			class="mt-3 inline-block rounded-xl bg-green-600 px-4 py-2 font-bold text-white"
		>
			View Orders
		</a>
	{/if}
{/if}