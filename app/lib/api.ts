
import axios from 'axios'
import { 
  ApplicationDto, 
  ExecutionDto, 
  ResultPublishedDto,
  OverallExecutionSummary,
  ApplicationExecutionSummary,
  ApplicationResponse,
  ExecutionsByApplicationResponse,
  DailyExecutionSummary // Added DailyExecutionSummary
} from '../types'

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
})

// Application API
export const applicationApi = {
  register: (application: ApplicationDto): Promise<ApplicationDto> =>
    api.post('/applications/register', application).then(res => res.data),
  
  getAll: (): Promise<ApplicationResponse> =>
    api.get('/applications').then(res => res.data),
  
  getById: (applicationId: string): Promise<ApplicationDto> =>
    api.get(`/applications/${applicationId}`).then(res => res.data),
  
  getExecutions: (applicationId: string): Promise<ExecutionsByApplicationResponse> =>
    api.get(`/applications/${applicationId}/executions`).then(res => res.data),
  
  getByStream: (streamName: string): Promise<ApplicationResponse> =>
    api.get(`/applications/stream/${streamName}`).then(res => res.data),
  
  getByStreamAndCrew: (streamName: string, crewName: string): Promise<ApplicationResponse> =>
    api.get(`/applications/stream/${streamName}/crew/${crewName}`).then(res => res.data),
  
  getByCrew: (crewName: string): Promise<ApplicationResponse> =>
    api.get(`/applications/crew/${crewName}`).then(res => res.data),

  getDistinctStreams: (): Promise<string[]> =>
    api.get('/applications/streams').then(res => res.data),

  getDistinctCrews: (): Promise<string[]> =>
    api.get('/applications/crews').then(res => res.data),
}

// Execution API
export const executionApi = {
  publishResults: (execution: ExecutionDto): Promise<ResultPublishedDto> =>
    api.post('/executions/publish-results', execution).then(res => res.data),
  
  getByReference: (executionReference: string): Promise<ExecutionDto> =>
    api.get(`/executions/${executionReference}`).then(res => res.data),
}

// Dashboard API
export const dashboardApi = {
  getOverallSummary: (): Promise<OverallExecutionSummary> =>
    api.get('/dashboard/summary').then(res => res.data),
  
  getSuiteSummary: (suiteCategory: string): Promise<OverallExecutionSummary> =>
    api.get(`/dashboard/${suiteCategory}/suite-summary`).then(res => res.data),
  
  getExecutionSummary: (executionType: string): Promise<OverallExecutionSummary> =>
    api.get(`/dashboard/${executionType}/execution-summary`).then(res => res.data),
  
  getApplicationSummary: (applicationId: string): Promise<ApplicationExecutionSummary> =>
    api.get(`/dashboard/${applicationId}/summary`).then(res => res.data),
  
  getAllApplicationsSummary: (): Promise<ApplicationExecutionSummary[]> =>
    api.get('/dashboard/applications/summary').then(res => res.data),

  // New trend APIs
  getOverallDailyExecutionTrends: (): Promise<DailyExecutionSummary[]> =>
    api.get('/dashboard/trends-data/overall').then(res => res.data),

  getApplicationDailyExecutionTrends: (applicationId: string): Promise<DailyExecutionSummary[]> =>
    api.get(`/dashboard/trends/applications/${applicationId}`).then(res => res.data),
}
