import type { User, Workspace, Board } from "@/types";

export const mockUser: User = {
  id: "u1",
  name: "Jane Cooper",
  email: "jane@example.com",
};

const mockUsers: User[] = [
  mockUser,
  { id: "u2", name: "Alex Morgan", email: "alex@example.com" },
  { id: "u3", name: "Sam Wilson", email: "sam@example.com" },
];

export const mockWorkspaces: Workspace[] = [
  {
    id: "w1",
    name: "Acme Corp",
    ownerId: "u1",
    members: [
      { user: mockUsers[0], role: "Owner" },
      { user: mockUsers[1], role: "Member" },
      { user: mockUsers[2], role: "Viewer" },
    ],
    boardCount: 3,
  },
  {
    id: "w2",
    name: "Startup Labs",
    ownerId: "u2",
    members: [
      { user: mockUsers[1], role: "Owner" },
      { user: mockUsers[0], role: "Member" },
    ],
    boardCount: 1,
  },
];

export const mockBoards: Board[] = [
  {
    id: "b1",
    workspaceId: "w1",
    name: "Product Roadmap",
    columns: [
      {
        id: "c1", boardId: "b1", name: "Todo", order: 0,
        tasks: [
          { id: "t1", columnId: "c1", title: "Design new landing page", description: "Create mockups for the new marketing landing page with updated branding.", order: 0 },
          { id: "t2", columnId: "c1", title: "Set up CI/CD pipeline", description: "Configure GitHub Actions for automated testing and deployment.", order: 1 },
        ],
      },
      {
        id: "c2", boardId: "b1", name: "In Progress", order: 1,
        tasks: [
          { id: "t3", columnId: "c2", title: "Implement auth flow", description: "Add login, register and password reset functionality.", order: 0 },
        ],
      },
      {
        id: "c3", boardId: "b1", name: "Done", order: 2,
        tasks: [
          { id: "t4", columnId: "c3", title: "Project scaffolding", description: "Initial project setup with React, TypeScript and Tailwind.", order: 0 },
        ],
      },
    ],
  },
  {
    id: "b2",
    workspaceId: "w1",
    name: "Sprint Board",
    columns: [
      { id: "c4", boardId: "b2", name: "Backlog", order: 0, tasks: [] },
      { id: "c5", boardId: "b2", name: "In Progress", order: 1, tasks: [] },
      { id: "c6", boardId: "b2", name: "Review", order: 2, tasks: [] },
      { id: "c7", boardId: "b2", name: "Done", order: 3, tasks: [] },
    ],
  },
  {
    id: "b3",
    workspaceId: "w1",
    name: "Bug Tracker",
    columns: [
      { id: "c8", boardId: "b3", name: "New", order: 0, tasks: [] },
      { id: "c9", boardId: "b3", name: "Investigating", order: 1, tasks: [] },
      { id: "c10", boardId: "b3", name: "Fixed", order: 2, tasks: [] },
    ],
  },
  {
    id: "b4",
    workspaceId: "w2",
    name: "MVP Tracker",
    columns: [
      { id: "c11", boardId: "b4", name: "Todo", order: 0, tasks: [] },
      { id: "c12", boardId: "b4", name: "Doing", order: 1, tasks: [] },
      { id: "c13", boardId: "b4", name: "Done", order: 2, tasks: [] },
    ],
  },
];
