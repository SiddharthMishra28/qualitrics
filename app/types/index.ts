
export interface ApplicationDto {
  id?: number
  applicationId: string
  applicationName: string
  applicationDescription: string
  stream: string
  crew: string
}

export interface ExecutionDto {
  applicationId: string
  executionType: 'functional' | 'regression'
  executionSuiteCategory: 'sanity' | 'smoke'
  totalTestCases: number
  countPassed: number
  countFailed: number
  countSkipped: number
  overallBuildStatus: string
  executionTime: number
  reportLink: string
  uuid: string
  createdAt: string
}

export interface ResultPublishedDto {
  publishResult: string
  executionReferenceId: string
}

export interface OverallExecutionSummary {
  totalBuilds: number
  totalBuildsPassed: number
  totalBuildsFailed: number
  percentPassed: number
  percentFailed: number
}

export interface ApplicationExecutionSummary {
  applicationId: string
  applicationName: string
  totalBuilds: number
  totalBuildsPassed: number
  totalBuildsFailed: number
  percentPassed: number
  percentFailed: number
}

export interface ApplicationResponse {
  applications: ApplicationDto[]
}

export interface ExecutionsByApplicationResponse {
  applicationId: string
  applicationName: string
  executions: ExecutionDto[]
}

export interface DailyExecutionSummary {
  date: string
  totalBuilds: number
  passed: number
  failed: number
}
