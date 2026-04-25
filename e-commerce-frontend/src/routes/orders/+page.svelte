<script>
	import OrderCard from '$lib/components/OrderCard.svelte';
	import StorefrontShell from '$lib/components/StorefrontShell.svelte';

	let { data } = $props();
</script>

<svelte:head>
	<title>Order History</title>
</svelte:head>

<StorefrontShell categories={data.categories}>
	<div class="space-y-8">
		<section
			class="rounded-[2rem] bg-[linear-gradient(135deg,#0f172a_0%,#1e293b_55%,#475569_100%)] px-6 py-10 text-white shadow-xl lg:px-10"
		>
			<p class="text-sm font-semibold tracking-[0.35em] text-slate-300 uppercase">Orders</p>
			<h1 class="mt-3 text-4xl font-black tracking-tight sm:text-5xl">Your order history</h1>
			<p class="mt-4 max-w-2xl text-base leading-7 text-slate-200">
				Review your recent purchases, order totals, and shipment status in one place.
			</p>
		</section>

		{#if data.error}
			<section class="rounded-[2rem] border border-red-200 bg-red-50 p-8 text-red-700 shadow-sm">
				<h2 class="text-xl font-bold">We couldn’t load your orders.</h2>
				<p class="mt-2 text-sm">{data.error}</p>
			</section>
		{:else if !data.orders.length}
			<section class="rounded-[2rem] border border-slate-200 bg-white p-10 text-center shadow-sm">
				<h2 class="text-2xl font-bold text-slate-950">No orders yet</h2>
				<p class="mt-3 text-sm text-slate-600">
					When you place your first order, it will show up here with its status and line items.
				</p>
				<a class="cta-button mt-6 inline-flex" href="/products">Start browsing</a>
			</section>
		{:else}
			<section class="space-y-5">
				{#each data.orders as order}
					<OrderCard {order} />
				{/each}
			</section>
		{/if}
	</div>
</StorefrontShell>
