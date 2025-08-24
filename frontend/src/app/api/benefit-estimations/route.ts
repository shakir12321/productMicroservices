import { NextResponse } from "next/server";

// Dummy data for benefit estimations
const dummyEstimations = [
  {
    id: 1,
    orderId: 1,
    benefitType: "LOYALTY_POINTS",
    estimatedValue: 150.0,
    status: "APPLIED",
    createdAt: "2024-08-24T09:05:00Z",
  },
  {
    id: 2,
    orderId: 2,
    benefitType: "CASHBACK",
    estimatedValue: 75.5,
    status: "CALCULATED",
    createdAt: "2024-08-24T10:35:00Z",
  },
  {
    id: 3,
    orderId: 3,
    benefitType: "DISCOUNT",
    estimatedValue: 200.0,
    status: "PENDING",
    createdAt: "2024-08-24T11:20:00Z",
  },
  {
    id: 4,
    orderId: 4,
    benefitType: "FREE_SHIPPING",
    estimatedValue: 45.25,
    status: "APPLIED",
    createdAt: "2024-08-24T12:05:00Z",
  },
  {
    id: 5,
    orderId: 5,
    benefitType: "LOYALTY_POINTS",
    estimatedValue: 120.75,
    status: "CALCULATED",
    createdAt: "2024-08-24T13:50:00Z",
  },
];

export async function GET() {
  try {
    // Try to connect to the actual microservice first
    const response = await fetch(
      "http://benefit-estimation-service:8083/api/benefit-estimations",
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    if (response.ok) {
      const data = await response.json();
      return NextResponse.json({ data });
    }
  } catch (error) {
    console.log("Microservice not available, using dummy data");
  }

  // Return dummy data if microservice is not available
  return NextResponse.json({ data: dummyEstimations });
}

export async function POST(request: Request) {
  try {
    const body = await request.json();

    // Try to connect to the actual microservice first
    const response = await fetch(
      "http://benefit-estimation-service:8083/api/benefit-estimations",
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
      }
    );

    if (response.ok) {
      const data = await response.json();
      return NextResponse.json(data);
    }
  } catch (error) {
    console.log("Microservice not available, using dummy response");
  }

  // Return dummy response if microservice is not available
  const newEstimation = {
    id: Math.floor(Math.random() * 1000) + 6,
    ...body,
    createdAt: new Date().toISOString(),
  };

  return NextResponse.json(newEstimation);
}
