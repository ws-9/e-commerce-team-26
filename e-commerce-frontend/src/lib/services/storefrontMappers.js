export function deriveRole(data) {
	if (data?.isSeller) return 'SELLER';
	if (data?.isAdmin) return 'ADMIN';
	if (data?.role) return data.role;
	if (data?.userRole) return data.userRole;
	return 'SHOPPER';
}
