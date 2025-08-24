import axios from "axios";

// API base URLs for microservices
const API_BASE_URLS = {
  product: process.env.NEXT_PUBLIC_PRODUCT_SERVICE_URL || "/api",
  order: process.env.NEXT_PUBLIC_ORDER_SERVICE_URL || "/api",
  benefitEstimation:
    process.env.NEXT_PUBLIC_BENEFIT_ESTIMATION_SERVICE_URL || "/api",
  payout: process.env.NEXT_PUBLIC_PAYOUT_SERVICE_URL || "/api",
};

// Create axios instances for each service
export const productApi = axios.create({
  baseURL: API_BASE_URLS.product,
  timeout: 10000,
});

export const orderApi = axios.create({
  baseURL: API_BASE_URLS.order,
  timeout: 10000,
});

export const benefitEstimationApi = axios.create({
  baseURL: API_BASE_URLS.benefitEstimation,
  timeout: 10000,
});

export const payoutApi = axios.create({
  baseURL: API_BASE_URLS.payout,
  timeout: 10000,
});

// Types
export interface Product {
  id?: number;
  name: string;
  description: string;
  price: number;
  stockQuantity: number;
  category: string;
}

export interface OrderItem {
  productId: number;
  quantity: number;
  price: number;
}

export interface Order {
  id?: number;
  customerName: string;
  customerEmail: string;
  items: OrderItem[];
  totalAmount: number;
  status: "PENDING" | "CONFIRMED" | "SHIPPED" | "DELIVERED" | "CANCELLED";
  createdAt?: string;
}

export interface BenefitEstimation {
  id?: number;
  orderId: number;
  benefitType: "LOYALTY_POINTS" | "CASHBACK" | "DISCOUNT" | "FREE_SHIPPING";
  estimatedValue: number;
  status: "PENDING" | "CALCULATED" | "APPLIED";
  createdAt?: string;
}

export interface Payout {
  id?: number;
  benefitEstimationId: number;
  amount: number;
  payoutMethod: "BANK_TRANSFER" | "CREDIT_CARD" | "DIGITAL_WALLET";
  status: "PENDING" | "PROCESSING" | "COMPLETED" | "FAILED";
  createdAt?: string;
}

// Product API functions
export const productApiFunctions = {
  getAllProducts: () => productApi.get<{ data: Product[] }>("/products"),
  getProductById: (id: number) => productApi.get<Product>(`/products/${id}`),
  createProduct: (product: Product) =>
    productApi.post<Product>("/products", product),
  updateProduct: (id: number, product: Product) =>
    productApi.put<Product>(`/products/${id}`, product),
  deleteProduct: (id: number) => productApi.delete(`/products/${id}`),
  getProductsByCategory: (category: string) =>
    productApi.get<{ data: Product[] }>(`/products/category/${category}`),
  searchProducts: (name: string) =>
    productApi.get<{ data: Product[] }>(`/products/search?name=${name}`),
  getProductsInStock: () =>
    productApi.get<{ data: Product[] }>("/products/in-stock"),
  updateStock: (id: number, quantity: number) =>
    productApi.patch(`/products/${id}/stock?quantity=${quantity}`),
  reserveStock: (id: number, quantity: number) =>
    productApi.post(`/products/${id}/reserve?quantity=${quantity}`),
};

// Order API functions
export const orderApiFunctions = {
  getAllOrders: () => orderApi.get<{ data: Order[] }>("/orders"),
  getOrderById: (id: number) => orderApi.get<Order>(`/orders/${id}`),
  createOrder: (order: Order) => orderApi.post<Order>("/orders", order),
  updateOrder: (id: number, order: Order) =>
    orderApi.put<Order>(`/orders/${id}`, order),
  deleteOrder: (id: number) => orderApi.delete(`/orders/${id}`),
  getOrdersByStatus: (status: string) =>
    orderApi.get<{ data: Order[] }>(`/orders/status/${status}`),
};

// Benefit Estimation API functions
export const benefitEstimationApiFunctions = {
  getAllEstimations: () =>
    benefitEstimationApi.get<{ data: BenefitEstimation[] }>(
      "/benefit-estimations"
    ),
  getEstimationById: (id: number) =>
    benefitEstimationApi.get<BenefitEstimation>(`/benefit-estimations/${id}`),
  createEstimation: (estimation: BenefitEstimation) =>
    benefitEstimationApi.post<BenefitEstimation>(
      "/benefit-estimations",
      estimation
    ),
  getEstimationsByOrderId: (orderId: number) =>
    benefitEstimationApi.get<{ data: BenefitEstimation[] }>(
      `/benefit-estimations/order/${orderId}`
    ),
  getEstimationsByStatus: (status: string) =>
    benefitEstimationApi.get<{ data: BenefitEstimation[] }>(
      `/benefit-estimations/status/${status}`
    ),
};

// Payout API functions
export const payoutApiFunctions = {
  getAllPayouts: () => payoutApi.get<{ data: Payout[] }>("/payouts"),
  getPayoutById: (id: number) => payoutApi.get<Payout>(`/payouts/${id}`),
  createPayout: (payout: Payout) => payoutApi.post<Payout>("/payouts", payout),
  getPayoutsByStatus: (status: string) =>
    payoutApi.get<{ data: Payout[] }>(`/payouts/status/${status}`),
  getPayoutsByEstimationId: (estimationId: number) =>
    payoutApi.get<{ data: Payout[] }>(`/payouts/estimation/${estimationId}`),
};
