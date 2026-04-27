export function deriveRole(data) {
	if (data?.role) return data.role;
	if (data?.userRole) return data.userRole;
	if (data?.seller) return 'SELLER';
	if (data?.admin) return 'ADMIN';
	return 'SHOPPER';
}
